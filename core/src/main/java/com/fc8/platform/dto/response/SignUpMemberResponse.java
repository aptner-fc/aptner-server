package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class SignUpMemberResponse {

    private final Long memberId;
    private final String apartContact;

    public SignUpMemberResponse(Long memberId, String apartContact) {
        this.memberId = memberId;
        this.apartContact = apartContact;
    }
}
