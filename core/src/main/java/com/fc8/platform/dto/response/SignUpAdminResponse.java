package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class SignUpAdminResponse {

    private final Long adminId;

    public SignUpAdminResponse(Long adminId) {
        this.adminId = adminId;
    }
}
