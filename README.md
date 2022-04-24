# easy-http
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.wendy512/easy-http/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.wendy512/easy-http/)
[![License](https://img.shields.io/github/license/alibaba/fastjson2?color=4D7A97&logo=apache)](https://www.apache.org/licenses/LICENSE-2.0.html)

easy http，最好用的http工具，支持多种http框架，目前支持apache httpclient、okhttp，简单配置，支持定义配置。
## 集成
如果你使用 Maven，你只需要在 pom.xml 中添加下面的依赖：
```xml  
<dependency>
    <groupId>io.github.wendy512</groupId>
    <artifactId>easy-http-client</artifactId>
    <version>1.0.0</version>
</dependency>
``` 
## 如何使用
同步请求
```java
HttpClientConfig clientConfig = HttpClientConfig.builder().httpClientWay(HttpClientWay.OKHTTP).build();
IHttpClient httpClient = HttpClientBuilder.create().config(clientConfig).build();
HttpRequest request = new HttpRequest.Builder().url("http://youhost").get().build();
HttpResponse response = httpClient.newCall(request).execute();
```
异步请求
```java
HttpClientConfig clientConfig = HttpClientConfig.builder().httpClientWay(HttpClientWay.OKHTTP).build();
IHttpClient httpClient = HttpClientBuilder.create().config(clientConfig).build();
HttpRequest request = new HttpRequest.Builder().url("http://youhost").get().build();
httpClient.newCall(request).execute(new IHttpClientCallback() {
    @Override
    public void onFailure(Exception e) {
        //do something
    }

    @Override
    public void onResponse(HttpResponse response) {
        //do something
    }
});
```
## 自定义配置
okhttp自定义配置
```java
public class CustomOkHttpConfigureHandler extends OkHttpConfigureHandler {
    @Override
    public void configure(HttpClientConfig config, OkHttpClient.Builder builder) {
        //do configure
        builder.readTimeout(Duration.ofSeconds(100));
    }
}

HttpClientConfig clientConfig = HttpClientConfig.builder().httpClientWay(HttpClientWay.OKHTTP)
            .configureCustomHandler(new CustomOkHttpConfigureHandler()).build();
```

apache httpclient 自定义配置
```java
public class CustomApacheConfigureHandler extends ApacheConfigureHandler {

    @Override
    public void configure(HttpClientConfig config, HttpClientBuilder builder) {
        //do configure
        builder.setMaxConnPerRoute(100);
    }

    @Override
    public void configure(HttpClientConfig config, HttpAsyncClientBuilder builder) {
        //do configure
        builder.setMaxConnPerRoute(100);
    }
}

HttpClientConfig clientConfig = HttpClientConfig.builder().httpClientWay(HttpClientWay.APACHE_CLIENT)
            .configureCustomHandler(new CustomApacheConfigureHandler()).build();
```

