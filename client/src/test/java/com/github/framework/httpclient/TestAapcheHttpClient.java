package com.github.framework.httpclient;

import com.github.framework.httpclient.config.HttpClientConfig;
import com.github.framework.httpclient.core.HttpRequest;
import com.github.framework.httpclient.core.HttpRequestBody;
import com.github.framework.httpclient.core.HttpResponse;
import com.github.framework.httpclient.core.IHttpClient;
import com.github.framework.httpclient.core.apache.HttpClient;
import org.junit.Test;

/**
 * 测试
 * 
 * @author wendy512
 * @date 2022-04-17 21:12:21:12
 * @since 1.0.0
 */
public class TestAapcheHttpClient {
    private final HttpClientConfig clientConfig = HttpClientConfig.builder().build();

    @Test
    public void testGet() {
        IHttpClient httpClient = new HttpClient.Builder().config(clientConfig).build();
        HttpRequest request = new HttpRequest.Builder().url("http://localhost:9091/test/get").get().build();
        HttpResponse response = httpClient.newCall(request).execute();
        System.out.println(String.format("http code: %s, http response: %s", response.code(), response.asText()));
    }

    @Test
    public void testPost() {
        IHttpClient httpClient = new HttpClient.Builder().config(clientConfig).build();
        String json = "{\"name\":\"zhangsan\"}";
        HttpRequest request =
            new HttpRequest.Builder().url("http://localhost:9091/test/post").post(HttpRequestBody.create(json)).build();
        HttpResponse response = httpClient.newCall(request).execute();
        System.out.println(String.format("http code: %s, http response: %s", response.code(), response.asText()));
    }

}
