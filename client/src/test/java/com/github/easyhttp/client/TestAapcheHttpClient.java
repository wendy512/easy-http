package com.github.easyhttp.client;

import com.github.easyhttp.client.config.HttpClientConfig;
import com.github.easyhttp.client.config.HttpClientProxyConfig;
import com.github.easyhttp.client.core.*;
import com.github.easyhttp.client.core.apache.HttpClient;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 测试
 * 
 * @author wendy512
 * @date 2022-04-17 21:12:21:12
 * @since 1.0.0
 */
public class TestAapcheHttpClient {
    private IHttpClient httpClient;

    @Before
    public void init() {
        HttpClientProxyConfig proxyConfig = HttpClientProxyConfig.builder().enable(false).ip("127.0.0.1").port(8888).build();
        HttpClientConfig clientConfig = HttpClientConfig.builder().proxyConfig(proxyConfig).build();

        httpClient = new HttpClient.Builder().config(clientConfig).build();
    }

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
                new HttpRequest.Builder().url("http://127.0.0.1:9091/test/post").post(HttpRequestBody.create(json)).build();
        httpClient.newCall(request).execute(new IHttpClientCallback() {
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(HttpResponse response) {
                System.out.println(String.format("http code: %s, http response: %s", response.code(), response.asText()));
            }
        });

        // 不能结束太快
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void testForm() {
        Map<String,String> form = new HashMap<>();
        form.put("name", "zhangsan");
        form.put("age", "12");
        HttpRequest request =
                new HttpRequest.Builder().url("http://127.0.0.1:9091/test/form").post(HttpRequestBody.create(form)).build();
        HttpResponse response = httpClient.newCall(request).execute();
        System.out.println(String.format("http code: %s, http response: %s", response.code(), response.asText()));
    }

    @Test
    public void testUpload() {
        List<HttpRequestMultipartBody.Part> parts = new ArrayList<>();
        parts.add(new HttpRequestMultipartBody.TextPart("name", "zhangsan"));
        try {
            parts.add(new HttpRequestMultipartBody.FilePart("file", new File("C:\\Users\\Lenovo\\Pictures\\Camera Roll\\demo.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpRequest request =
                new HttpRequest.Builder().url("http://127.0.0.1:9091/test/upload").post(HttpRequestBody.create(parts)).build();
        HttpResponse response = httpClient.newCall(request).execute();
        System.out.println(String.format("http code: %s, http response: %s", response.code(), response.asText()));

    }

    @Test
    public void testAsyncUpload() {
        List<HttpRequestMultipartBody.Part> parts = new ArrayList<>();
        parts.add(new HttpRequestMultipartBody.TextPart("name", "zhangsan"));
        try {
            parts.add(new HttpRequestMultipartBody.FilePart("file",
                new File("C:\\Users\\Lenovo\\Pictures\\Camera Roll\\demo.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpRequest request = new HttpRequest.Builder().url("http://127.0.0.1:9091/test/upload")
            .post(HttpRequestBody.create(parts)).build();
        httpClient.newCall(request).execute((response) -> {
            System.out.println(String.format("http code: %s, http response: %s", response.code(), response.asText()));
        });
    }

}
