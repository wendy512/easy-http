package com.github.easyhttp.client.core;

import lombok.Getter;

/**
 * text 类型的请求body
 * @author wendy512
 * @date 2022-04-16 16:03:16:03
 * @since 1.0.0
 */
@Getter
public class HttpRequestTextBody extends HttpRequestBody {
    private final String content;

    public HttpRequestTextBody(String contextType, String content) {
        this.contextType = contextType;
        this.content = content;
    }
}
