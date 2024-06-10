package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class DeleteDisclosureCommentResponse {

    private final Long DisclosureCommentId;

    public DeleteDisclosureCommentResponse(Long disclosureCommentId) {
        DisclosureCommentId = disclosureCommentId;
    }
}
