package com.fc8.aptner.common.handler;

import com.fc8.aptner.common.exception.BaseException;
import com.fc8.aptner.common.exception.code.ErrorCode;
import com.fc8.aptner.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonResponse<String>> handle(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        return CommonResponse.fail(errorCode);
    }

}
