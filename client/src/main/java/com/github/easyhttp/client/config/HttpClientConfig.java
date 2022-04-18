package com.github.easyhttp.client.config;

import lombok.Builder;
import lombok.Getter;

/**
 * http client配置参数
 * 
 * @author wendy512
 * @date 2022-04-16 14:59:14:59
 * @since 1.0.0
 */
@Builder
@Getter
public class HttpClientConfig {
    /**
     * 使用哪种连接框架
     */
    private HttpClientWay httpClientWay;
    /**
     * 连接超时时间，单位：秒
     */
    @Builder.Default
    private long connectTimeout = 60;
    /**
     * 读取超时时间，单位：秒
     */
    @Builder.Default
    private long readTimeout = 60;
    /**
     * 写入超时时间，单位：秒
     */
    @Builder.Default
    private long writeTimeout = 60;
    /**
     * 保持连接的最大时间，单位：秒
     */
    @Builder.Default
    private long keepAlive = 180;
    /**
     * 最大空闲的连接数
     */
    @Builder.Default
    private int maxIdleConnections = 5;

    /**
     * 最大的连接数
     */
    @Builder.Default
    private int maxConnections = 500;
    /**
     * 请求是否压缩
     */
    @Builder.Default
    private boolean compressionEnabled = true;

    /**
     * 启用重定向
     */
    @Builder.Default
    private boolean redirectsEnabled = true;

    /**
     * 代理配置
     */
    @Builder.Default
    private HttpClientProxyConfig proxyConfig;

    /**
     * 需要特殊配置需求
     */
    @Builder.Default
    private ConfigureCustomHandler configureCustomHandler;
}
