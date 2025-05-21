package com.fifo.ticketing.global.exception;

import lombok.Getter;

@Getter
public class AlertDetailException extends RuntimeException {

    private final String url;
    private final String message;
    private final ErrorCode errorCode;

    public AlertDetailException(ErrorCode errorCode, String message) {
        this.url = null;
        this.message = message;
        this.errorCode = errorCode;
    }

    public AlertDetailException(String url, String message, ErrorCode errorCode) {
        this.url = url;
        this.message = message;
        this.errorCode = errorCode;
    }
}
