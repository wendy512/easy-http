package com.github.easyhttp.client.core.okhttp3;

import com.github.easyhttp.client.core.*;
import com.github.easyhttp.client.exception.HttpException;

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
            return httpClient.syncDispatcher().execute(request);
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    public void execute(IHttpClientCallback responseCallback) {
        try {
            httpClient.asyncDispatcher().execute(request, responseCallback);
        } catch (Exception e) {
            throw new HttpException(e);
        }
    }
}
