package com.github.easyhttp.client.core;

import lombok.Getter;

import java.util.Map;

/**
 * from表单形式的请求体
 * @author wendy512
 * @date 2022-04-16 15:46:15:46
 * @since 1.0.0
 */
@Getter
public class HttpRequestFormBody extends HttpRequestBody {
    private final Map<String,String> fromData;

    public HttpRequestFormBody(String contextType, Map<String, String> fromData) { 
        this.contextType = contextType;
        this.fromData = fromData;
    }
}
