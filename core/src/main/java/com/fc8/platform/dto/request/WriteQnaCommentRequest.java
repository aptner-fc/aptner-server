package com.fc8.platform.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WriteQnaCommentRequest {

    @Positive
    private Long parentId;

    @Size(min = 1, max = 500, message = "댓글은 500글자를 초과할 수 없습니다.")
    private String content;

}
