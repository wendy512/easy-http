package com.github.framework.httpclient.core;

import java.io.IOException;

/**
 * 请求派发
 * 
 * @author wendy512
 * @date 2022-04-16 17:17:17:17
 * @since 1.0.0
 */
public interface IDispatcher {
    HttpResponse execute(HttpRequest request) throws IOException;

    void enqueue(HttpRequest request, IHttpClientCallback responseCallback) throws IOException;
}
