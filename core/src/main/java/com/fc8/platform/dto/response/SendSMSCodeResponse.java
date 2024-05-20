package com.fc8.platform.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SendSMSCodeResponse {

    private final String code;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime expiredAt;

    public SendSMSCodeResponse(String code, LocalDateTime expiredAt) {
        this.code = code;
        this.expiredAt = expiredAt;
    }
}
