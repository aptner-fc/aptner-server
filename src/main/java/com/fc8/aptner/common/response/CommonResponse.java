package com.fc8.aptner.common.response;

import com.fc8.aptner.common.exception.code.ErrorCode;
import com.fc8.aptner.common.exception.code.SuccessCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private final boolean success;
    private final String message;
    private final T result;

    @Builder
    public CommonResponse(boolean success, String message, T result) {
        this.success = success;
        this.message = message;
        this.result = result;

    }

    public static <T> CommonResponse<T> success(SuccessCode successCode) {
        return new CommonResponse<>(true, successCode.getMessage(), null);
    }

    public static <T> CommonResponse<T> success(SuccessCode successCode, T data) {
        return new CommonResponse<>(true, successCode.getMessage(), data);
    }

    public static <T> CommonResponse<T> fail(ErrorCode errorCode) {
        return new CommonResponse<>(false, errorCode.getMessage(), null);
    }

    public static <T> CommonResponse<T> fail(ErrorCode errorCode, T data) {
        return new CommonResponse<>(false, errorCode.getMessage(), data);
    }

}
