package com.github.framework.httpclient.core.okhttp3;

import com.github.framework.httpclient.config.ConfigureCustomHandler;
import com.github.framework.httpclient.config.HttpClientConfig;
import com.github.framework.httpclient.config.HttpClientProxyConfig;
import com.github.framework.httpclient.core.*;
import okhttp3.Authenticator;
import okhttp3.ConnectionPool;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * okhttp3 http client 实现
 * 
 * @author wendy512
 * @date 2022-04-17 15:11:15:11
 * @since 1.0.0
 */
public class HttpClient implements IHttpClient {
    private final HttpClientConfig config;
    private final IDispatcher dispatcher;
    private final OkHttpClient realHttpClient;

    public HttpClient(Builder builder) {
        this.config = builder.config;
        this.realHttpClient = createClient();
        this.dispatcher = new Dispatcher(realHttpClient);
    }

    public IHttpClientCall newCall(HttpRequest request) {
        return new HttpClientCall(request, this);
    }

    public IDispatcher dispatcher() {
        return this.dispatcher;
    }

    private OkHttpClient createClient() {
        HttpClientProxyConfig proxyConfig = config.getProxyConfig();
        ConnectionPool pool =
            new ConnectionPool(config.getMaxIdleConnections(), config.getKeepAlive(), TimeUnit.SECONDS);
        OkHttpClient.Builder builder =
            new OkHttpClient.Builder().connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                .followRedirects(config.isRedirectsEnabled())
                .followSslRedirects(config.isRedirectsEnabled()).connectionPool(pool);
                //.addInterceptor(new GzipInterceptor(config.isCompressionEnabled()));
        if (null != proxyConfig && proxyConfig.isEnable()) {
            //组织代理配置
            builder
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.getIp(), proxyConfig.getPort())));
            if (StringUtils.isNotBlank(proxyConfig.getUsername()) && StringUtils.isNotBlank(proxyConfig.getPassword())) {
                Authenticator authenticator = (route, response) -> {
                    String credential = Credentials.basic(proxyConfig.getUsername(), proxyConfig.getPassword());
                    return response.request().newBuilder().header(HttpHeaders.PROXY_AUTHORIZATION, credential).build();
                };
                builder.proxyAuthenticator(authenticator);
            }
        }

        //可以进行自定义配置
        ConfigureCustomHandler configureCustomHandler = config.getConfigureCustomHandler();
        if (null != configureCustomHandler) {
            configureCustomHandler.configure(builder);
        }
        return builder.build();
    }

    public static final class Builder {
        private HttpClientConfig config;

        public Builder() {}

        public Builder config(HttpClientConfig config) {
            this.config = config;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }
}
