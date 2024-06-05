package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class WriteQnaAnswerResponse {

    private final Long QnaAnswerId;

    public WriteQnaAnswerResponse(Long qnaAnswerId) {
        QnaAnswerId = qnaAnswerId;
    }
}
