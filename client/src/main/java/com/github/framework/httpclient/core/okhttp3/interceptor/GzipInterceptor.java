package com.github.framework.httpclient.core.okhttp3.interceptor;

import com.github.framework.httpclient.core.HttpHeaders;
import okhttp3.*;
import okhttp3.internal.http.RealResponseBody;
import okio.BufferedSink;
import okio.GzipSink;
import okio.GzipSource;
import okio.Okio;

import java.io.IOException;

/**
 * 对响应内容进行解压
 * 
 * @author wendy512
 * @date 2021-06-27 14:12:14:12
 * @since 1.0.0
 */
public class GzipInterceptor implements Interceptor {
    public static final String COMPRESSION_GZIP = "gzip";
    /**
     * 请求是否启用压缩
     */
    private final boolean requestCompressionEnabled;

    public GzipInterceptor(boolean requestCompressionEnabled) {
        this.requestCompressionEnabled = requestCompressionEnabled;
    }

    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        RequestBody body = originalRequest.body();
        if (body == null || originalRequest.header(HttpHeaders.CONTENT_ENCODING) != null) {
            return chain.proceed(originalRequest);
        }

        
        if (requestCompressionEnabled) {
            Request compressedRequest = originalRequest.newBuilder().header(HttpHeaders.CONTENT_ENCODING, COMPRESSION_GZIP)
                    .method(originalRequest.method(), gzip(body)).build();
            Response response = chain.proceed(compressedRequest);
            return unzip(response, chain);
        } else {
            Response response = chain.proceed(originalRequest);
            return unzip(response, chain);
        }
    }
    
    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    } 

    private Response unzip(final Response response, Chain chain) throws IOException {
        ResponseBody body = response.body();
        if (body == null) {
            return response;
        }

        String contentEncoding = response.headers().get(HttpHeaders.CONTENT_ENCODING);
        if (contentEncoding != null && COMPRESSION_GZIP.equals(contentEncoding)
            && okhttp3.internal.http.HttpHeaders.hasBody(response)) {
            Long contentLength = body.contentLength();
            GzipSource responseBody = new GzipSource(body.source());
            Headers strippedHeaders = response.headers().newBuilder().build();
            String contextType = response.header(HttpHeaders.CONTENT_TYPE);
            return response.newBuilder().headers(strippedHeaders)
                .body(new RealResponseBody(contextType, -1L, Okio.buffer(responseBody))).build();
        } else {
            return response;
        }
    }
}
