package com.github.easyhttp.client;

import com.github.easyhttp.client.config.HttpClientConfig;
import com.github.easyhttp.client.core.apache.ApacheConfigureHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

/**
 * @author wendy512
 * @date 2022-04-23 16:40:16:40
 * @since 1.0.0
 */
public class CustomApacheConfigureHandler extends ApacheConfigureHandler {

    @Override
    public void configure(HttpClientConfig config, HttpClientBuilder builder) {
        //do configure
        builder.setMaxConnPerRoute(100);
    }

    @Override
    public void configure(HttpClientConfig config, HttpAsyncClientBuilder builder) {
        //do configure
        builder.setMaxConnPerRoute(100);
    }
}
