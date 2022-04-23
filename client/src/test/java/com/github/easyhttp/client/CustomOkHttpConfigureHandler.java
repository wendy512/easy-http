package com.github.easyhttp.client;

import com.github.easyhttp.client.config.HttpClientConfig;
import com.github.easyhttp.client.core.okhttp3.OkHttpConfigureHandler;

import okhttp3.OkHttpClient;

import java.time.Duration;

/**
 * okhttp 自定义配置
 * @author wendy512
 * @date 2022-04-23 16:33:16:33
 * @since 1.0.0
 */
public class CustomOkHttpConfigureHandler extends OkHttpConfigureHandler {
    @Override
    public void configure(HttpClientConfig config, OkHttpClient.Builder builder) {
        //do configure
        builder.readTimeout(Duration.ofSeconds(100));
    }
}
