package com.github.easyhttp.client.core.okhttp3;

import com.github.easyhttp.client.core.HttpRequest;
import com.github.easyhttp.client.core.HttpResponse;
import com.github.easyhttp.client.core.IDispatcher;
import com.github.easyhttp.client.core.IHttpClientCallback;
import okhttp3.*;

import java.io.IOException;

/**
 * 派发者，真实执行请求
 * @author wendy512
 * @date 2022-04-16 17:26:17:26
 * @since 1.0.0
 */
public class Dispatcher implements IDispatcher {
    private final OkHttpClient httpClient;

    public Dispatcher(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        Request realRequest = RequestFactory.create(request);
        try (Response response = httpClient.newCall(realRequest).execute()){
            return ResponseFactory.create(response);
        }
    }

    @Override
    public void execute(HttpRequest request, IHttpClientCallback responseCallback) throws IOException {
        Request realRequest = RequestFactory.create(request);
        httpClient.newCall(realRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseCallback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseCallback.onResponse(ResponseFactory.create(response));
            }
        });
    }
}
