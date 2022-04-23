package com.github.easyhttp.client.core;

/**
 * http client call，支持同步和异步
 * 
 * @author wendy512
 * @date 2022-04-16 17:04:17:04
 * @since 1.0.0
 */
public interface IHttpClientCall {
    HttpRequest request();

    HttpResponse execute();

    void execute(IHttpClientCallback responseCallback);
}
