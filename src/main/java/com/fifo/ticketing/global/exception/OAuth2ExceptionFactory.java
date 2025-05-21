package com.fifo.ticketing.global.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

public class OAuth2ExceptionFactory {

    public static OAuth2AuthenticationException fromErrorCode(ErrorCode errorCode) {
        return new OAuth2AuthenticationException(
            new OAuth2Error(errorCode.getCode(), errorCode.getMessage(), null),
            errorCode.getMessage()
        );
    }
}
