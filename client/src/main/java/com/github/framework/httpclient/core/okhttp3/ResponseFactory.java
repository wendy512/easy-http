package com.github.framework.httpclient.core.okhttp3;

import com.github.framework.httpclient.core.HttpResponse;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * okhttp client response factory
 * 
 * @author wendy512
 * @date 2022-04-17 21:01:21:01
 * @since 1.0.0
 */
public final class ResponseFactory {
    private ResponseFactory() {}

    public static HttpResponse create(Response realResponse) {
        Headers headers = realResponse.headers();
        HttpResponse response = HttpResponse.builder().code(realResponse.code()).body(realResponse.body().byteStream())
            .headers(headers.toMultimap()).build();
        return response;
    }
}
