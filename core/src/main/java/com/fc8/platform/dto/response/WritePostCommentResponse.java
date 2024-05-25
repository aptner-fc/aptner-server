package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class WritePostCommentResponse {

    private final Long postCommentId;

    public WritePostCommentResponse(Long postCommentId) {
        this.postCommentId = postCommentId;
    }
}
