package org.apache.http.entity.mime;

import org.apache.http.ContentTooLongException;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 由于MultipartFormEntity有大小限制，自定义entity
 * @author wendy512
 * @date 2022-04-23 13:40:13:40
 * @since 1.0.0
 */
public class MultipartFormExtEntity extends MultipartFormEntity {

    private final long contentLength;
    
    public MultipartFormExtEntity(AbstractMultipartForm multipart, ContentType contentType, long contentLength) {
        super(multipart, contentType, contentLength);
        this.contentLength = contentLength;
    }

    @Override
    public InputStream getContent() throws IOException {
        if (this.contentLength < 0) {
            throw new ContentTooLongException("Content length is unknown");
        }
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        writeTo(outStream);
        outStream.flush();
        return new ByteArrayInputStream(outStream.toByteArray());
    }
}
