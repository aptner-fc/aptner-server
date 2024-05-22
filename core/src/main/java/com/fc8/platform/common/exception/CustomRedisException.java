package com.fc8.platform.common.exception;

import com.fc8.platform.common.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class CustomRedisException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomRedisException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomRedisException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}