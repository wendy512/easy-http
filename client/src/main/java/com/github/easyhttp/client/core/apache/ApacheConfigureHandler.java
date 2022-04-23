package com.github.easyhttp.client.core.apache;


import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

import com.github.easyhttp.client.config.ConfigureHandler;
import com.github.easyhttp.client.config.HttpClientConfig;

/**
 * 配置抽象类，适配一下配置类型
 * @author wendy512
 * @date 2022-04-18 16:23:16:23
 * @since 1.0.0
 */
public abstract class ApacheConfigureHandler implements ConfigureHandler {
    @Override
    public void configure(HttpClientConfig config, Object builder) {
        if (builder instanceof HttpClientBuilder) {
            configure(config, (HttpClientBuilder) builder);
        } else {
            configure(config, (HttpAsyncClientBuilder) builder);
        }
    }
    
    public abstract void configure(HttpClientConfig config, HttpClientBuilder builder);

    public abstract void configure(HttpClientConfig config, HttpAsyncClientBuilder builder);
}
