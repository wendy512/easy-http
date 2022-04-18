package com.github.easyhttp.client.core;

/**
 * http context type
 * @author wendy512
 * @date 2022-04-16 15:39:15:39
 * @since 1.0.0
 */
public enum HttpContextType {
    JSON("application/json"), FROM_URLENCODED("application/x-www-form-urlencoded"), multipart_FORM_DATA("multipart/form-data");
    private String contextType;

    HttpContextType(String contextType) {
        this.contextType = contextType;
    }

    public String getContextType() {
        return contextType;
    }
}
