package com.github.easyhttp.client.core.apache.sync;

import com.github.easyhttp.client.core.HttpRequest;
import com.github.easyhttp.client.core.HttpResponse;
import com.github.easyhttp.client.core.IDispatcher;
import com.github.easyhttp.client.core.apache.ResponseFactory;
import com.github.easyhttp.client.core.apache.RequestFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * 同步派发者，真实执行请求
 * @author wendy512
 * @date 2022-04-16 17:26:17:26
 * @since 1.0.0
 */
public class SyncDispatcher implements IDispatcher {
    private final CloseableHttpClient syncHttpClient;

    public SyncDispatcher(CloseableHttpClient realSyncHttpClient) {
        this.syncHttpClient = realSyncHttpClient;
    }
    
    public HttpResponse execute(HttpRequest request) throws IOException {
        HttpRequestBase realRequest = RequestFactory.create(request);
        CloseableHttpResponse response = syncHttpClient.execute(realRequest);
        return ResponseFactory.create(response);
    }

    protected CloseableHttpClient getSyncHttpClient() {
        return this.syncHttpClient;
    }
}
