package com.fc8.platform.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePhoneRequest {

    @NotNull(message = "전화번호가 누락되었습니다.")
    private String phone;

    @NotNull(message = "인증 코드가 누락되었습니다.")
    private String verificationCode;

    @NotNull(message = "전화번호가 누락되었습니다.")
    private String newPhone;
}
