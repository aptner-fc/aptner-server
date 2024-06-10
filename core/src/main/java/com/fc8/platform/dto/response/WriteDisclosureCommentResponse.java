package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class WriteDisclosureCommentResponse {

    private final Long DisclosureCommentId;

    public WriteDisclosureCommentResponse(Long disclosureCommentId) {
        DisclosureCommentId = disclosureCommentId;
    }
}
