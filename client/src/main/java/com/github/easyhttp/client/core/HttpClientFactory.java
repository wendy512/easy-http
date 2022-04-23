package com.github.easyhttp.client.core;

import com.github.easyhttp.client.config.HttpClientConfig;
import com.github.easyhttp.client.config.HttpClientWay;
import com.github.easyhttp.client.core.okhttp3.HttpClient;

/**
 * HttpClient 工厂类
 * @author wendy512
 * @date 2022-04-23 16:23:16:23
 * @since 1.0.0
 */
public final class HttpClientFactory {
    private HttpClientConfig clientConfig;

    public static IHttpClient create(HttpClientConfig config) {
        if (config.getHttpClientWay() == HttpClientWay.APACHE_CLIENT) {
            return new com.github.easyhttp.client.core.apache.HttpClient.Builder().config(config).build();
        } else {
            return new HttpClient.Builder().config(config).build();
        }
    }
}
