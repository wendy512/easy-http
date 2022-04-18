package com.github.framework.httpclient.core;

/**
 * http client 接口
 * 
 * @author wendy512
 * @date 2022-04-16 15:04:15:04
 * @since 1.0.0
 */
public interface IHttpClient {

    /**
     * 执行http call
     * @param request http请求数据
     * @return 请求响应call，支持同步、异步
     */
    IHttpClientCall newCall(HttpRequest request);

    /**
     * 获取派发者，真正执行http的
     * @return 对象实例
     */
    IDispatcher dispatcher();
}