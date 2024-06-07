package com.fc8.platform.dto.request;

import com.fc8.platform.domain.enums.ProcessingStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WriteQnaAnswerRequest {

    @Size(min = 1, max = 500, message = "답변은 500글자를 초과할 수 없습니다.")
    private String content;

    @NotNull(message = "처리 상태가 누락되었습니다.")
    private ProcessingStatus processingStatus;

}
