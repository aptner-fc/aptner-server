package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class WriteQnaCommentResponse {

    private final Long QnaCommentId;

    public WriteQnaCommentResponse(Long qnaCommentId) {
        QnaCommentId = qnaCommentId;
    }
}
