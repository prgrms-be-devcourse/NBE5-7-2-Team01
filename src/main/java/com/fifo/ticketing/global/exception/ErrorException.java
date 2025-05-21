package com.fifo.ticketing.global.exception;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException {

    private final String url;
    private final ErrorCode errorCode;

    public ErrorException(ErrorCode errorCode) {
        this.url = null;
        this.errorCode = errorCode;
    }

    public ErrorException(String url, ErrorCode errorCode) {
        this.url = url;
        this.errorCode = errorCode;
    }
}