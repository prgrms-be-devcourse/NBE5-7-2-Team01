package com.fifo.ticketing.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"code", "message", "errors"})
public class ErrorResponse<T> {

    private final String code;
    private final String message;

    @JsonInclude(Include.NON_EMPTY)
    private final T errors;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.errors = null;
    }
}
