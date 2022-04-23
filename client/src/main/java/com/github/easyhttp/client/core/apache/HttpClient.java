package com.github.easyhttp.client.core.apache;

import com.github.easyhttp.client.config.ConfigureHandler;
import com.github.easyhttp.client.config.HttpClientConfig;
import com.github.easyhttp.client.config.HttpClientProxyConfig;
import com.github.easyhttp.client.core.HttpRequest;
import com.github.easyhttp.client.core.IDispatcher;
import com.github.easyhttp.client.core.IHttpClient;
import com.github.easyhttp.client.core.IHttpClientCall;
import com.github.easyhttp.client.core.apache.async.AsyncDispatcher;
import com.github.easyhttp.client.core.apache.sync.SyncDispatcher;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import java.util.concurrent.locks.ReentrantLock;

/**
 * apache http client 实现
 * 
 * @author wendy512
 * @date 2022-04-16 17:22:17:22
 * @since 1.0.0
 */
public class HttpClient implements IHttpClient {
    private final ReentrantLock lock = new ReentrantLock(true);
    private final HttpClientConfig config;
    private final IDispatcher syncDispatcher;
    private IDispatcher asyncDispatcher;
    private final CloseableHttpClient syncClient;
    private CloseableHttpAsyncClient asyncClient; 

    public HttpClient(Builder builder) {
        this.config = builder.config;
        this.syncClient = createSyncClient();
        this.syncDispatcher = new SyncDispatcher(this.syncClient);
    }

    public IHttpClientCall newCall(HttpRequest request) {
        return new HttpClientCall(request, this);
    }

    public IDispatcher syncDispatcher() {
        return this.syncDispatcher;
    }

    @Override
    public IDispatcher asyncDispatcher() {
        return getAsyncDispatcher();
    }

    /**
     * 初始化
     * @return
     */
    private CloseableHttpClient createSyncClient() {
        HttpClientProxyConfig proxyConfig = config.getProxyConfig();
        RequestConfig.Builder configBuilder = createRequestConfigBuilder(config);

        HttpClientBuilder clientBuilder = HttpClients.custom();
        HttpHost proxyHost = createProxyHost(proxyConfig);
        BasicCredentialsProvider proxyAuthenticator = createProxyAuthenticator(proxyConfig);
        clientBuilder.setProxy(proxyHost);
        clientBuilder.setDefaultCredentialsProvider(proxyAuthenticator);

        //连接池配置
        PoolingHttpClientConnectionManager connectPool = createConnectionPool(config);
        clientBuilder.setConnectionManager(connectPool);

        RequestConfig requestConfig = configBuilder.build();
        clientBuilder.setDefaultRequestConfig(requestConfig);
        //可以进行自定义配置
        ConfigureHandler configureCustomHandler = config.getConfigureCustomHandler();
        if (null != configureCustomHandler) {
            configureCustomHandler.configure(config, clientBuilder);
        }
        return clientBuilder.build();
    }

    private PoolingHttpClientConnectionManager createConnectionPool(HttpClientConfig config) {
        PoolingHttpClientConnectionManager connectPool = new PoolingHttpClientConnectionManager();
        connectPool.setMaxTotal(config.getMaxConnections());
        return connectPool;
    }

    private HttpHost createProxyHost(HttpClientProxyConfig proxyConfig) {
        //代理host
        if (null != proxyConfig && proxyConfig.isEnable()) {
            HttpHost proxy = new HttpHost(proxyConfig.getIp(), proxyConfig.getPort());
            return proxy;
        }
        return null;
    }
    
    private BasicCredentialsProvider createProxyAuthenticator(HttpClientProxyConfig proxyConfig) {
        //代理鉴权配置
        if (null != proxyConfig && proxyConfig.isEnable()) {
            if (StringUtils.isNotBlank(proxyConfig.getUsername()) && StringUtils.isNotBlank(proxyConfig.getPassword())) {
                BasicCredentialsProvider provider = new BasicCredentialsProvider();
                provider.setCredentials(new AuthScope(proxyConfig.getIp(), proxyConfig.getPort()),
                        new UsernamePasswordCredentials(proxyConfig.getUsername(),
                                proxyConfig.getPassword()));
                return provider;
            }
        }
        return null;
    }


    private RequestConfig.Builder createRequestConfigBuilder(HttpClientConfig config) {
        //基础配置
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(toMillisecond(config.getConnectTimeout()))
            .setConnectionRequestTimeout(toMillisecond(config.getWriteTimeout()))
            .setSocketTimeout(toMillisecond(config.getReadTimeout()))
            .setContentCompressionEnabled(config.isCompressionEnabled())
            .setRedirectsEnabled(config.isRedirectsEnabled());
        return configBuilder;
    }

    /**
     * 获取异步配发者
     * @return
     */
    public IDispatcher getAsyncDispatcher() {
        if (null == this.asyncClient) {
            try {
                lock.lock();
                //double check
                if (null == this.asyncClient) {
                    this.asyncClient = createAsyncClient();
                    this.asyncClient.start();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
            this.asyncDispatcher = new AsyncDispatcher(this.syncClient, this.asyncClient);
        }
        return this.asyncDispatcher;
    }
    
    private CloseableHttpAsyncClient createAsyncClient() {
        HttpClientProxyConfig proxyConfig = config.getProxyConfig();
        HttpAsyncClientBuilder clientBuilder = HttpAsyncClients.custom();
        
        RequestConfig.Builder configBuilder = createRequestConfigBuilder(config);
        HttpHost proxyHost = createProxyHost(proxyConfig);
        BasicCredentialsProvider proxyAuthenticator = createProxyAuthenticator(proxyConfig);
        clientBuilder.setProxy(proxyHost);
        clientBuilder.setDefaultCredentialsProvider(proxyAuthenticator);
        //reactor配置
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setConnectTimeout(toMillisecond(config.getConnectTimeout()))
                .setSoTimeout(toMillisecond(config.getReadTimeout()))
                .setTcpNoDelay(true)
                .setSelectInterval(100)
                .build();
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            throw new RuntimeException(e);
        }
        PoolingNHttpClientConnectionManager connectPool = new PoolingNHttpClientConnectionManager(ioReactor);
        clientBuilder.setConnectionManager(connectPool);
        clientBuilder.setDefaultRequestConfig(configBuilder.build());
        //可以进行自定义配置
        ConfigureHandler configureCustomHandler = config.getConfigureCustomHandler();
        if (null != configureCustomHandler) {
            configureCustomHandler.configure(config, clientBuilder);
        }
        return clientBuilder.build();
    }

    public static final class Builder {
        private HttpClientConfig config;
        private SyncDispatcher dispatcher;


        public Builder config(HttpClientConfig config) {
            this.config = config;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }
    
    private int toMillisecond(int second) {
        return second * 1000;
    }
}
