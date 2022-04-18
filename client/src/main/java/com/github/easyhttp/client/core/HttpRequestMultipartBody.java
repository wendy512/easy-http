package com.github.easyhttp.client.core;

import lombok.Getter;

import java.util.List;

/**
 * from表单包含文件形式的请求体
 * 
 * @author wendy512
 * @date 2022-04-16 15:46:15:46
 * @since 1.0.0
 */
@Getter
public class HttpRequestMultipartBody extends HttpRequestBody {
    private final List<Part> parts;

    public HttpRequestMultipartBody(String contextType, List<Part> parts) {
        this.contextType = contextType;
        this.parts = parts;
    }

    public static abstract class Part {
        private final String name;
        private final Object value;

        public Part(String name, Object value) {
            this.name = name;
            this.value = value;
        }
        
        public String name() {
            return this.name;
        }
        
        public Object value() {
            return this.value;
        }
    }

    public static class TextPart extends Part {
        public TextPart(String name, String value) {
            super(name, value);
        }
    }

    public static class FilePart extends Part {
        private String fileName;

        public FilePart(String name, byte[] value) {
            super(name, value);
        }

        public FilePart(String name, String fileName, byte[] value) {
            super(name, value);
            this.fileName = fileName;
        }

        public String fileName() {
            return this.fileName;
        }
    }
}
