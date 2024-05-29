package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class DeletePostResponse {

    private final Long postId;

    public DeletePostResponse(Long postId) {
        this.postId = postId;
    }
}
