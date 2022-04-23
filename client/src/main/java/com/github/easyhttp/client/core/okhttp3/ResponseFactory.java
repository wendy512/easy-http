package com.github.easyhttp.client.core.okhttp3;

import com.github.easyhttp.client.core.HttpResponse;
import com.github.easyhttp.client.exception.HttpException;
import okhttp3.Headers;
import okhttp3.Response;
import okio.BufferedSource;

import java.io.IOException;
import java.io.InputStream;

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
        InputStream stream = realResponse.body().byteStream();
        BufferedSource source = realResponse.body().source();
        try {
            HttpResponse response = HttpResponse.builder().code(realResponse.code()).body(source.readByteArray())
                .headers(headers.toMultimap()).build();
            return response;
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }
}
