package com.github.framework.httpclient.core.okhttp3;

import com.github.framework.httpclient.core.*;
import com.github.framework.httpclient.exception.HttpException;

import java.io.IOException;

/**
 * http client call，支持同步和异步的默认实现
 *
 * @author wendy512
 * @date 2022-04-16 17:04:17:04
 * @since 1.0.0
 */
public class HttpClientCall implements IHttpClientCall {
    private final HttpRequest request;
    private final IHttpClient httpClient;

    public HttpClientCall(HttpRequest originalRequest, IHttpClient httpClient) {
        this.request = originalRequest;
        this.httpClient = httpClient;
    }

    public HttpRequest request() {
        return this.request;
    }

    public HttpResponse execute() {
        try {
            return httpClient.dispatcher().execute(request);
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    public void enqueue(IHttpClientCallback responseCallback) {
        try {
            httpClient.dispatcher().enqueue(request, responseCallback);
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }
}
