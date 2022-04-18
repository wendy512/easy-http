package com.github.framework.httpclient.config;

import lombok.Builder;
import lombok.Getter;

/**
 * http client代理配置参数
 * @author wendy512
 * @date 2022-04-16 15:02:15:02
 * @since 1.0.0
 */
@Builder
@Getter
public class HttpClientProxyConfig {
    /**
     * 是否启动，默认不启用代理
     */
    @Builder.Default
    private boolean enable = false;
    /**
     * 代理IP
     */
    private String ip;
    /**
     * 代理端口
     */
    private int port;
    /**
     * 代理鉴权用户名
     */
    private String username;
    /**
     * 代理鉴权密码
     */
    private String password;
}
