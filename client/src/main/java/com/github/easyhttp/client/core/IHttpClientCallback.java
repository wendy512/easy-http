package com.github.easyhttp.client.core;

import java.io.IOException;

/**
 * http client callback回调
 * 
 * @author wendy512
 * @date 2022-04-16 17:05:17:05
 * @since 1.0.0
 */
public interface IHttpClientCallback {
    /**
     * 失败回调
     * @param e
     */
    void onFailure(IOException e);

    /**
     * 响应回调
     * @param response
     * @throws IOException
     */
    void onResponse(HttpResponse response) throws IOException;
}
