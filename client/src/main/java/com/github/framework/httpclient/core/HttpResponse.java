package com.github.framework.httpclient.core;

import lombok.Builder;
import org.apache.commons.codec.Charsets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private InputStream body;

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
    
    public String asText() {
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(body, Charsets.toCharset("UTF-8"));
            int n;
            StringBuilderWriter output = new StringBuilderWriter();
            char[] buffer = new char[8192];
            while (-1 != (n = in.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (null != body) body.close();
                if (null != in) in.close();
                //help gc
                this.body = null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public InputStream asStream() {
        InputStream temp = body;
        //help gc
        this.body = null;
        return temp;
    }
}
