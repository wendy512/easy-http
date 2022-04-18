package com.github.easyhttp.client.core.apache;

import com.github.easyhttp.client.core.IDispatcher;
import com.github.easyhttp.client.core.IHttpClientCall;
import com.github.easyhttp.client.config.ConfigureCustomHandler;
import com.github.easyhttp.client.config.HttpClientConfig;
import com.github.easyhttp.client.config.HttpClientProxyConfig;
import com.github.easyhttp.client.core.HttpRequest;
import com.github.easyhttp.client.core.IHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.TimeValue;

import java.util.concurrent.TimeUnit;

/**
 * apache http client 实现
 * 
 * @author wendy512
 * @date 2022-04-16 17:22:17:22
 * @since 1.0.0
 */
public class HttpClient implements IHttpClient {
    private final HttpClientConfig config;
    private final IDispatcher dispatcher;
    private final CloseableHttpClient realHttpClient;

    public HttpClient(Builder builder) {
        this.config = builder.config;
        this.realHttpClient = createClient();
        this.dispatcher = new Dispatcher(this.realHttpClient);
    }

    public IHttpClientCall newCall(HttpRequest request) {
        return new HttpClientCall(request, this);
    }

    public IDispatcher dispatcher() {
        return this.dispatcher;
    }

    /**
     * 初始化
     * @return
     */
    private CloseableHttpClient createClient() {
        HttpClientProxyConfig proxyConfig = config.getProxyConfig();
        //基础配置
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
            .setConnectionRequestTimeout(config.getWriteTimeout(), TimeUnit.SECONDS)
            .setResponseTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
            .setContentCompressionEnabled(config.isCompressionEnabled())
            .setRedirectsEnabled(config.isRedirectsEnabled())
            .setConnectionKeepAlive(TimeValue.of(config.getKeepAlive(), TimeUnit.SECONDS));

        HttpClientBuilder clientBuilder = HttpClients.custom();
        //代理配置
        if (null != proxyConfig && proxyConfig.isEnable()) {
            clientBuilder.setProxy(new HttpHost(proxyConfig.getIp(), proxyConfig.getPort()));
            if (StringUtils.isNotBlank(proxyConfig.getUsername()) && StringUtils.isNotBlank(proxyConfig.getPassword())) {
                BasicCredentialsProvider provider = new BasicCredentialsProvider();
                provider.setCredentials(new AuthScope(proxyConfig.getIp(), proxyConfig.getPort()),
                        new UsernamePasswordCredentials(proxyConfig.getUsername(),
                                proxyConfig.getPassword().toCharArray()));
                clientBuilder.setDefaultCredentialsProvider(provider);
            }
        }

        //连接池配置
        PoolingHttpClientConnectionManager connectPool =
            PoolingHttpClientConnectionManagerBuilder.create().setMaxConnTotal(config.getMaxConnections()).build();
        clientBuilder.setConnectionManager(connectPool);

        //可以进行自定义配置
        ConfigureCustomHandler configureCustomHandler = config.getConfigureCustomHandler();
        if (null != configureCustomHandler) {
            configureCustomHandler.configure(clientBuilder);
        }

        clientBuilder.setDefaultRequestConfig(configBuilder.build());
        return clientBuilder.build();
    }

    public static final class Builder {
        private HttpClientConfig config;


        public Builder config(HttpClientConfig config) {
            this.config = config;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }

}
