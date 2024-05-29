package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class WriteQnaResponse {

    private final Long qnaId;

    public WriteQnaResponse(Long qnaId) {
        this.qnaId = qnaId;
    }
}
