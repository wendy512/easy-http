package com.github.easyhttp.client.core;

import lombok.Builder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * http 请求的响应结果
 * 
 * @author wendy512
 * @date 2022-04-16 15:18:15:18
 * @since 1.0.0
 */
@Builder
public class HttpResponse {
    /**
     * http状态码
     */
    private int code;
    
    /**
     * 响应数据
     */
    private byte[] body;

    /**
     * 响应头，header name -> values
     */
    private Map<String, List<String>> headers;

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }
    
    public int code() {
        return this.code;
    }
    
    public String asText(Charset charset) {
        String text = new String(this.body, charset);
        //help gc
        this.body = null;
        return text;
    }

    public String asText() {
        String text = new String(this.body, Charset.forName("UTF-8"));
        //help gc
        this.body = null;
        return text;
    }
    
    public byte[] asBytes() {
        byte[] temp = body;
        //help gc
        this.body = null;
        return temp;
    }
}
