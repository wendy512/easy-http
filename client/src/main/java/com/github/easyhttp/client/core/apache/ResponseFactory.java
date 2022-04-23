package com.github.easyhttp.client.core.apache;

import com.github.easyhttp.client.core.HttpResponse;
import com.github.easyhttp.common.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHttpResponse;

import java.io.IOException;
import java.io.InputStream;
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
    
    private static final int BUFFER_SIZE = 8192;

    public static HttpResponse create(org.apache.http.HttpResponse realResponse) {
        Header[] realHeaders = realResponse.getAllHeaders();
        Map<String, List<String>> headers = new LinkedHashMap<>(realHeaders.length);
        for (Header header : realHeaders) {
            String value = header.getValue();
            String[] values = value.split(";");
            headers.put(header.getName(), Arrays.asList(values));
        }

        HttpEntity entity = null;
        //同步响应
        if (realResponse instanceof CloseableHttpResponse) {
            entity = ((CloseableHttpResponse) realResponse).getEntity();
         //异步响应
        } else {
            entity = ((BasicHttpResponse) realResponse).getEntity();
        }

        InputStream in = null;
        try {
            in = entity.getContent();
            byte[] bytes = IOUtils.toByteArray(in);
            HttpResponse response = HttpResponse.builder().code(realResponse.getStatusLine().getStatusCode())
                .headers(headers).body(bytes).build();
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    
}
