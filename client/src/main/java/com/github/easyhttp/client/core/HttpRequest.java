package com.github.easyhttp.client.core;

import com.github.easyhttp.client.constant.HttpMethod;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * http request参数
 * 
 * @author wendy512
 * @date 2022-04-16 15:30:15:30
 * @since 1.0.0
 */
public class HttpRequest {
    private final String url;
    private final HttpMethod method;
    private final HttpRequestBody body;
    private Map<String, String> headers = Collections.emptyMap();

    public HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.body = builder.body;
        this.headers = builder.headers;
    }

    public static class Builder {
        private String url;
        private HttpMethod method;
        private HttpRequestBody body;
        private Map<String, String> headers;
        private String encoding;

        public Builder() {
            this.method = HttpMethod.GET;
            this.encoding = "utf-8";
            this.headers = new LinkedHashMap<String, String>(0);
        }

        public Builder(HttpRequest request) {
            this.url = request.url;
            this.method = request.method;
            this.body = request.body;
            this.headers = request.headers;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder encoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public Builder method(HttpMethod method, HttpRequestBody body) {
            this.method = method;
            this.body = body;
            return this;
        }

        public Builder get() {
            return method(HttpMethod.GET, null);
        }

        public Builder post(HttpRequestBody body) {
            return method(HttpMethod.POST, body);
        }

        public Builder head() {
            return method(HttpMethod.HEAD, null);
        }

        public Builder delete(HttpRequestBody body) {
            return method(HttpMethod.DELETE, body);
        }

        public Builder patch(HttpRequestBody body) {
            return method(HttpMethod.PATCH, body);
        }
        
        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

    public String url() {
        return url;
    }

    public HttpMethod method() {
        return method;
    }

    public HttpRequestBody body() {
        return body;
    }

    public Map<String, String> headers() {
        return headers;
    }
}
