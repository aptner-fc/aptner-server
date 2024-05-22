package com.fc8.platform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VerifySMSCodeResponse {

    private final boolean isVerify;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long attempts;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final LocalDateTime expiredAt;

    public static VerifySMSCodeResponse verify(String code, LocalDateTime expiredAt) {
        return new VerifySMSCodeResponse(true, null, code, expiredAt);
    }

    public static VerifySMSCodeResponse fail(Long attempts) {
        return new VerifySMSCodeResponse(false, attempts, null, null);
    }

    private VerifySMSCodeResponse(boolean isVerify, Long attempts, String code, LocalDateTime expiredAt) {
        this.isVerify = isVerify;
        this.attempts = attempts;
        this.code = code;
        this.expiredAt = expiredAt;
    }
}
