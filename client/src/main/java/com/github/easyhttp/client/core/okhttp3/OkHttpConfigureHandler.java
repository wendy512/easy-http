package com.github.easyhttp.client.core.okhttp3;


import org.apache.http.impl.client.HttpClientBuilder;

import com.github.easyhttp.client.config.ConfigureHandler;
import com.github.easyhttp.client.config.HttpClientConfig;

/**
 * 配置抽象类，适配一下配置类型
 * 
 * @author wendy512
 * @date 2022-04-18 16:23:16:23
 * @since 1.0.0
 */
public abstract class OkHttpConfigureHandler implements ConfigureHandler {
    @Override
    public void configure(HttpClientConfig config, Object builder) {
        configure(config, builder);
    }

    public abstract void configure(HttpClientConfig config, HttpClientBuilder builder);
}
