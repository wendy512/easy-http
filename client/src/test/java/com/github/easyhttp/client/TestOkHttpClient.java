package com.github.easyhttp.client;

import com.github.easyhttp.client.config.HttpClientConfig;
import com.github.easyhttp.client.core.*;
import com.github.easyhttp.client.core.okhttp3.HttpClient;
import com.github.framework.easyhttp.core.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 测试
 * 
 * @author wendy512
 * @date 2022-04-17 21:12:21:12
 * @since 1.0.0
 */
public class TestOkHttpClient {
    private final IHttpClient httpClient = new HttpClient.Builder().config(HttpClientConfig.builder().build()).build();

    @Test
    public void testGet() {
        HttpRequest request = new HttpRequest.Builder().url("http://localhost:9091/test/get").get().build();
        HttpResponse response = httpClient.newCall(request).execute();
        System.out.println(String.format("http code: %s, http response: %s", response.code(), response.asText()));
    }

    @Test
    public void testPost() {
        String json = "{\"name\":\"zhangsan\"}";
        HttpRequest request =
            new HttpRequest.Builder().url("http://localhost:9091/test/post").post(HttpRequestBody.create(json)).build();
        HttpResponse response = httpClient.newCall(request).execute();
        System.out.println(String.format("http code: %s, http response: %s", response.code(), response.asText()));
    }

    @Test
    public void testAsyncPost() throws Exception {
        String json = "{\"name\":\"zhangsan\"}";
        HttpRequest request =
                new HttpRequest.Builder().url("http://localhost:9091/test/post").post(HttpRequestBody.create(json)).build();
        httpClient.newCall(request).enqueue(new IHttpClientCallback() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(HttpResponse response) throws IOException {
                System.out.println(String.format("http code: %s, http response: %s", response.code(), response.asText()));
            }
        });

        // 不能结束太快
        TimeUnit.SECONDS.sleep(2);
    }
}
