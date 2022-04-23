package com.github.easyhttp.client.config;

/**
 * 使用者可能需要特殊配置需求，BuilderType针对使用的框架不同</br>
 * okhttp 对应okhttp3.OkHttpClient.Builder
 * apache client 对应org.apache.hc.client5.http.impl.classic.HttpClientBuilder
 * @author wendy512
 * @date 2022-04-17 16:20:16:20
 * @since 1.0.0
 */
public interface ConfigureHandler {
    /**
     * 自定义配置
     * @param config 配置数据
     * @param builder 对应框架的builder
     */
    void configure(HttpClientConfig config, Object builder);
}
