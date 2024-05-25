package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class CreateQnaResponse {

    private final Long qnaId;

    public CreateQnaResponse(Long qnaId) {
        this.qnaId = qnaId;
    }
}
