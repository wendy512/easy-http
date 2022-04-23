package com.github.easyhttp.client.core.apache;

import com.github.easyhttp.client.constant.HttpHeaders;
import com.github.easyhttp.client.constant.HttpMethod;
import com.github.easyhttp.client.core.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityExtBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * apache http client request factory
 * 
 * @author wendy512
 * @date 2022-04-17 17:04:17:04
 * @since 1.0.0
 */
public final class RequestFactory {
    private RequestFactory() {}

    public static HttpRequestBase create(HttpRequest request) {
        assert null != request;
        HttpMethod method = request.method();
        HttpRequestBase realRequest = null;
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

        if (null != request.headers()) {
            for (Map.Entry<String, String> header : request.headers().entrySet()) {
                realRequest.addHeader(header.getKey(), header.getValue());
            }
        }
        return realRequest;
    }

    private static HttpRequestBase createGet(HttpRequest request) {
        HttpGet get = new HttpGet(request.url());
        HttpRequestBody body = request.body();
        if (null == body) {
            return get;
        }

        setResource(get, body);
        return get;
    }

    private static HttpRequestBase createPost(HttpRequest request) {
        HttpPost post = new HttpPost(request.url());
        HttpRequestBody body = request.body();
        if (null == body) {
            return post;
        }

        setResource(post, body);
        return post;
    }

    private static void setResource(HttpRequestBase request, HttpRequestBody body) {
        if (body instanceof HttpRequestTextBody) {
            StringEntity entity = createTextEntity(body);
            if (null != entity) {
                ((HttpEntityEnclosingRequestBase)request).setEntity(entity);
            }
            request.addHeader(HttpHeaders.CONTENT_TYPE, body.getContextType());
        } else if (body instanceof HttpRequestFormBody) {
            UrlEncodedFormEntity fromEntity = createFromEntity(body);
            ((HttpEntityEnclosingRequestBase)request).setEntity(fromEntity);
        } else if (body instanceof HttpRequestMultipartBody) {
            HttpEntity entity = createMultipartEntity(body);
            ((HttpEntityEnclosingRequestBase)request).setEntity(entity);
        }
    }

    private static HttpRequestBase createHead(HttpRequest request) {
        HttpHead head = new HttpHead(request.url());
        HttpRequestBody body = request.body();
        if (null == body) {
            return head;
        }

        setResource(head, body);
        return head;
    }

    private static HttpRequestBase createPut(HttpRequest request) {
        HttpPut head = new HttpPut(request.url());
        HttpRequestBody body = request.body();
        if (null == body) {
            return head;
        }

        setResource(head, body);
        return head;
    }

    private static HttpRequestBase createPatch(HttpRequest request) {
        HttpPatch head = new HttpPatch(request.url());
        HttpRequestBody body = request.body();
        if (null == body) {
            return head;
        }

        setResource(head, body);
        return head;
    }

    private static HttpRequestBase createDelete(HttpRequest request) {
        HttpDelete head = new HttpDelete(request.url());
        HttpRequestBody body = request.body();
        if (null == body) {
            return head;
        }

        setResource(head, body);
        return head;
    }

    private static StringEntity createTextEntity(HttpRequestBody body) {
        String contextType = body.getContextType();
        String text = ((HttpRequestTextBody)body).getContent();
        if (StringUtils.isNotBlank(text)) {
            StringEntity entity = new StringEntity(text, body.getCharset());
            return entity;
        }
        return null;
    }

    private static UrlEncodedFormEntity createFromEntity(HttpRequestBody body) {
        Map<String, String> fromData = ((HttpRequestFormBody)body).getFromData();
        List<NameValuePair> pairs = new ArrayList<>();
        if (null != fromData) {
            for (Map.Entry<String, String> entry : fromData.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), null == entry.getValue() ? "" : entry.getValue()));
            }
        }

        return new UrlEncodedFormEntity(pairs, body.getCharset());
    }

    private static HttpEntity createMultipartEntity(HttpRequestBody body) {
        List<HttpRequestMultipartBody.Part> parts = ((HttpRequestMultipartBody)body).getParts();
        if (null != parts) {
            MultipartEntityExtBuilder entityBuilder = MultipartEntityExtBuilder.create();
            entityBuilder.setContentType(ContentType.create(body.getContextType(), body.getCharset()));
            for (HttpRequestMultipartBody.Part part : parts) {
                if (part instanceof HttpRequestMultipartBody.TextPart) {
                    entityBuilder.addTextBody(part.name(), (String)part.value());
                } else {
                    HttpRequestMultipartBody.FilePart filePart = (HttpRequestMultipartBody.FilePart)part;
                    entityBuilder.addPart(part.name(), new ByteArrayBody((byte[])filePart.value(),
                        ContentType.MULTIPART_FORM_DATA, filePart.fileName()));
                }
            }
            return entityBuilder.build();
        }

        return null;
    }

}
