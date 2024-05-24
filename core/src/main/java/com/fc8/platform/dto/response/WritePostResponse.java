package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class WritePostResponse {

    private final Long postId;

    public WritePostResponse(Long postId) {
        this.postId = postId;
    }
}
