package com.github.framework.httpclient.core.apache;

import com.github.framework.httpclient.core.HttpRequest;
import com.github.framework.httpclient.core.IDispatcher;
import com.github.framework.httpclient.core.IHttpClientCallback;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ClassicHttpRequest;

import java.io.IOException;

/**
 * 派发者，真实执行请求
 * @author wendy512
 * @date 2022-04-16 17:26:17:26
 * @since 1.0.0
 */
public class Dispatcher implements IDispatcher {
    private final CloseableHttpClient httpClient;

    public Dispatcher(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    
    public com.github.framework.httpclient.core.HttpResponse execute(HttpRequest request) throws IOException {
        ClassicHttpRequest realRequest = RequestFactory.create(request);
        CloseableHttpResponse response = httpClient.execute(realRequest);
        return ResponseFactory.create(response);
    }

    @Override
    public void enqueue(HttpRequest request, IHttpClientCallback responseCallback) throws IOException {
        
    }

}
