package com.github.easyhttp.client.core.apache;

import com.github.easyhttp.client.core.HttpResponse;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * apache http client response factory
 * 
 * @author wendy512
 * @date 2022-04-17 17:59:17:59
 * @since 1.0.0
 */
public class ResponseFactory {

    public static HttpResponse create(CloseableHttpResponse realResponse) {
        Header[] realHeaders = realResponse.getHeaders();
        Map<String, List<String>> headers = new LinkedHashMap<>(realHeaders.length);
        for (Header header : realHeaders) {
            String value = header.getValue();
            String[] values = value.split(";");
            headers.put(header.getName(), Arrays.asList(values));
        }

        try {
            HttpEntity entity = realResponse.getEntity();
            HttpResponse response = HttpResponse.builder().code(realResponse.getCode()).headers(headers).body(entity.getContent()).build();
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
