package com.fc8.platform.common.handler;


import com.fc8.platform.common.exception.BaseException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.common.exception.CustomRedisException;
import com.fc8.platform.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonResponse<String>> handle(BaseException e) {
        log.error("BaseException : {} ", e.getMessage());
        return CommonResponse.fail(e.getErrorCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse<String>> handle(RuntimeException e) {
        log.error("RuntimeException : {}", e.getMessage());
        return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(CustomRedisException.class)
    public ResponseEntity<CommonResponse<String>> handle(CustomRedisException e) {
        log.error("CustomRedisException : {} ", e.getMessage());
        return CommonResponse.fail(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<String>> handle(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : {}", e.getMessage());
        return CommonResponse.fail(ErrorCode.VALIDATION_ERROR, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

}
