package com.fc8.platform.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FindEmailRequest {

    @NotBlank(message = "이름이 누락되었습니다.")
    private String name;

    @NotBlank(message = "전화번호가 누락되었습니다.")
    private String phone;
}
