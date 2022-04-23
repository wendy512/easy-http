package com.github.easyhttp.client.core.apache.async;

import com.github.easyhttp.client.core.HttpRequest;
import com.github.easyhttp.client.core.IHttpClientCallback;
import com.github.easyhttp.client.core.apache.RequestFactory;
import com.github.easyhttp.client.core.apache.ResponseFactory;
import com.github.easyhttp.client.core.apache.sync.SyncDispatcher;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 异步派发者，真实执行请求
 * 
 * @author wendy512
 * @date 2022-04-16 17:26:17:26
 * @since 1.0.0
 */
public class AsyncDispatcher extends SyncDispatcher {

    protected CloseableHttpAsyncClient asyncHttpClient;

    public AsyncDispatcher(CloseableHttpClient syncHttpClient) {
        super(syncHttpClient);
    }

    public AsyncDispatcher(CloseableHttpClient syncHttpClient, CloseableHttpAsyncClient asyncHttpClient) {
        super(syncHttpClient);
        this.asyncHttpClient = asyncHttpClient;
    }

    @Override
    public void execute(HttpRequest request, IHttpClientCallback responseCallback) throws IOException {
        if (null == asyncHttpClient) {
            throw new IllegalArgumentException("asyncHttpClient must be init");
        }

        HttpRequestBase realRequest = RequestFactory.create(request);
        Future<HttpResponse> future = asyncHttpClient.execute(realRequest, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                responseCallback.onResponse(ResponseFactory.create(result));
            }

            @Override
            public void failed(Exception ex) {
                responseCallback.onFailure(ex);
            }

            @Override
            public void cancelled() {
            }
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
