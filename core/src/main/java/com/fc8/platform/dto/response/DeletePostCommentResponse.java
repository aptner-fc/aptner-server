package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class DeletePostCommentResponse {

    private final Long postCommentId;

    public DeletePostCommentResponse(Long postCommentId) {
        this.postCommentId = postCommentId;
    }
}
