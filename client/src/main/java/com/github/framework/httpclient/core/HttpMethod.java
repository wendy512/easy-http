package com.github.framework.httpclient.core;

/**
 * http method
 * 
 * @author wendy512
 * @date 2022-04-16 15:10:15:10
 * @since 1.0.0
 */
public enum HttpMethod {
    POST("post"), GET("get"), HEAD("head"), DELETE("delete"), PUT("put"), PATCH("patch");

    private String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
