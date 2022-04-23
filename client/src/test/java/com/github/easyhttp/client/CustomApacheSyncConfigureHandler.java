package com.github.easyhttp.client;

import com.github.easyhttp.client.config.HttpClientConfig;
import com.github.easyhttp.client.core.apache.sync.ApacheSyncConfigureHandler;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author wendy512
 * @date 2022-04-23 16:40:16:40
 * @since 1.0.0
 */
public class CustomApacheSyncConfigureHandler extends ApacheSyncConfigureHandler {

    @Override
    public void configure(HttpClientConfig config, HttpClientBuilder builder) {
        //do configure
        builder.setMaxConnPerRoute(100);
    }
}
