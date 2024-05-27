package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class DeleteQnaCommentResponse {

    private final Long qnaCommentId;

    public DeleteQnaCommentResponse(Long qnaCommentId) {
        this.qnaCommentId = qnaCommentId;
    }
}
