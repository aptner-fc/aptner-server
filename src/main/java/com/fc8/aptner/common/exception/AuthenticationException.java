package com.fc8.aptner.common.exception;

import com.fc8.aptner.common.exception.code.ErrorCode;

public class AuthenticationException extends BaseException {

    public AuthenticationException() {
        super(ErrorCode.INVALID_AUTHENTICATION);
    }

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthenticationException(String message) {
        super(message, ErrorCode.INVALID_AUTHENTICATION);
    }

    public AuthenticationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
