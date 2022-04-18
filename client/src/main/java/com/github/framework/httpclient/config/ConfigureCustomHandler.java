package com.github.framework.httpclient.config;

/**
 * 使用者可能需要特殊配置需求，BuilderType针对使用的框架不同</br>
 * okhttp3 对应okhttp3.OkHttpClient.Builder
 * apache client 对应org.apache.hc.client5.http.config.RequestConfig
 * @author wendy512
 * @date 2022-04-17 16:20:16:20
 * @since 1.0.0
 */
public interface ConfigureCustomHandler<BuilderType> {
    void configure(BuilderType builder);
}
