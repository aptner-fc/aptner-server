package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class DeleteQnaResponse {

    private final Long qnaId;

    public DeleteQnaResponse(Long qnaId) {
        this.qnaId = qnaId;
    }
}
