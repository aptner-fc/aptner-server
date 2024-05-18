package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class SavedTermsResponse {

    private final Long termsId;

    public SavedTermsResponse(Long termsId) {
        this.termsId = termsId;
    }
}
