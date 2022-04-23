package com.github.easyhttp.client.core;

import com.github.easyhttp.client.constant.HttpContextType;
import lombok.Getter;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * http request body参数
 * 
 * @author wendy512
 * @date 2022-04-16 15:33:15:33
 * @since 1.0.0
 */
@Getter
public abstract class HttpRequestBody {
    protected String contextType;
    protected Charset charset = Charset.forName("utf-8");

    public static HttpRequestBody create(String content) {
        return new HttpRequestTextBody(HttpContextType.JSON.getContextType(), content);
    }
    
    public static HttpRequestBody create(String contextType, String content) {
        return new HttpRequestTextBody(contextType, content);
    }

    public static HttpRequestBody create(Map<String, String> fromData) {
        return new HttpRequestFormBody(HttpContextType.FROM_URLENCODED.getContextType(), fromData);
    }

    public static HttpRequestBody create(List<HttpRequestMultipartBody.Part> parts) {
        return new HttpRequestMultipartBody(HttpContextType.multipart_FORM_DATA.getContextType(), parts);
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
