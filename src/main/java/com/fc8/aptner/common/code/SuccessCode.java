package com.fc8.aptner.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    SUCCESS(HttpStatus.OK, "성공했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
