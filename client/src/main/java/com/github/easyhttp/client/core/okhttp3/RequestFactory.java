package com.github.easyhttp.client.core.okhttp3;

import com.github.easyhttp.client.constant.HttpMethod;
import com.github.easyhttp.client.core.*;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * okhttp3 client request factory
 * 
 * @author wendy512
 * @date 2022-04-17 20:33:20:33
 * @since 1.0.0
 */
public final class RequestFactory {
    private RequestFactory() {}

    public static Request create(HttpRequest request) {
        assert null != request;
        HttpMethod method = request.method();
        Request realRequest = null;
        switch (method) {
            case GET:
                realRequest = createGet(request);
                break;
            case POST:
                realRequest = createPost(request);
                break;
            case HEAD:
                realRequest = createHead(request);
                break;
            case PUT:
                realRequest = createPut(request);
                break;
            case PATCH:
                realRequest = createPatch(request);
                break;
            case DELETE:
                realRequest = createDelete(request);
                break;
        }
        
        return realRequest;
    }

    private static Request createGet(HttpRequest request) {
        Request realRequest =
            new Request.Builder().url(request.url()).headers(Headers.of(request.headers())).get().build();
        return realRequest;
    }

    private static Request createPost(HttpRequest request) {
        HttpRequestBody body = request.body();
        Request realRequest = new Request.Builder().url(request.url()).headers(Headers.of(request.headers()))
            .post(createRequestBody(body)).build();
        return realRequest;
    }

    private static Request createHead(HttpRequest request) {
        HttpRequestBody body = request.body();
        Request realRequest =
            new Request.Builder().url(request.url()).headers(Headers.of(request.headers())).head().build();
        return realRequest;
    }

    private static Request createPut(HttpRequest request) {
        HttpRequestBody body = request.body();
        Request realRequest = new Request.Builder().url(request.url()).headers(Headers.of(request.headers()))
            .put(createRequestBody(body)).build();
        return realRequest;
    }

    private static Request createPatch(HttpRequest request) {
        HttpRequestBody body = request.body();
        Request realRequest = new Request.Builder().url(request.url()).headers(Headers.of(request.headers()))
            .patch(createRequestBody(body)).build();
        return realRequest;
    }

    private static Request createDelete(HttpRequest request) {
        HttpRequestBody body = request.body();
        Request realRequest = new Request.Builder().url(request.url()).headers(Headers.of(request.headers()))
            .delete(createRequestBody(body)).build();
        return realRequest;
    }

    private static RequestBody createRequestBody(HttpRequestBody body) {
        if (body instanceof HttpRequestTextBody) {
            return createTextBody(body);
        } else if (body instanceof HttpRequestFormBody) {
            return createFormBody((HttpRequestFormBody)body);
        } else if (body instanceof HttpRequestMultipartBody) {
            return createMultipartBody((HttpRequestMultipartBody)body);
        }
        return null;
    }

    private static MultipartBody createMultipartBody(HttpRequestMultipartBody body) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MediaType.parse(body.getContextType()));
        List<HttpRequestMultipartBody.Part> parts = body.getParts();
        if (null != parts) {
            for (HttpRequestMultipartBody.Part part : parts) {
                if (part instanceof HttpRequestMultipartBody.TextPart) {
                    builder.addFormDataPart(part.name(), (String)part.value());
                } else {
                    HttpRequestMultipartBody.FilePart filePart = (HttpRequestMultipartBody.FilePart)part;
                    builder.addFormDataPart(filePart.name(), filePart.fileName(),
                        RequestBody.create((byte[])filePart.value()));
                }
            }
        }
        return builder.build();
    }

    private static RequestBody createFormBody(HttpRequestFormBody body) {
        FormBody.Builder builder = new FormBody.Builder();
        Map<String, String> fromData = body.getFromData();
        if (null != fromData) {
            for (Map.Entry<String, String> entry : fromData.entrySet()) {
                builder.add(entry.getKey(), null == entry.getValue() ? "" : entry.getValue());
            }
        }
        return builder.build();
    }

    private static RequestBody createTextBody(HttpRequestBody body) {
        String content = ((HttpRequestTextBody)body).getContent();
        if (StringUtils.isNotEmpty(content)) {
            return RequestBody.create(MediaType.parse(body.getContextType()), content);
        } else {
            return RequestBody.create(MediaType.parse(body.getContextType()), "");
        }
    }
}
