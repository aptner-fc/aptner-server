package com.fc8.platform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WriteQnaRequest {

    @NotBlank(message = "카테고리가 누락되었습니다.")
    private String categoryCode;

    @Size(max = 50, message = "제목은 최대 50글자만 작성 가능합니다.")
    @NotBlank(message = "제목이 누락되었습니다.")
    private String title;

    @NotBlank(message = "내용이 누락되었습니다.")
    private String content;

    @NotBlank(message = "비밀글 여부가 누락되었습니다.")
    private Boolean isPrivate;

}
