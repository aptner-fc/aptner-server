package com.fc8.aptner.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record TokenInfo(String accessToken,
                        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") Date accessExpiredAt) {

}