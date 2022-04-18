package com.github.easyhttp.client.exception;

/**
 * 异常
 * 
 * @author wendy512
 * @date 2022-04-17 19:36:19:36
 * @since 1.0.0
 */
public class HttpException extends RuntimeException {
    public HttpException() {
        super();
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }
}
