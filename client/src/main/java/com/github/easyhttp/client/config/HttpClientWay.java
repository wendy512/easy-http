package com.github.easyhttp.client.config;

/**
 * http client使用方式
 * @author wendy512
 * @date 2022-04-16 15:06:15:06
 * @since 1.0.0
 */
public enum HttpClientWay {
    APACHE_CLIENT("apache-httpclient"),OKHTTP("okhttp3");
    private String way;

    HttpClientWay(String way) {
        this.way = way;
    }

    public String getWay() {
        return way;
    }
}
