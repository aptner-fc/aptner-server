package com.fc8.aptner.core.dto.response;

import lombok.Getter;

@Getter
public class SignUpMemberResponse {

    private final Long memberId;

    public SignUpMemberResponse(Long memberId) {
        this.memberId = memberId;
    }
}