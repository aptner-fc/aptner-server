package com.fc8.platform.dto.response;

import com.fc8.platform.dto.AdminInfo;
import com.fc8.platform.dto.TokenInfo;
import lombok.Getter;

@Getter
public class SignInAdminResponse {

    private final SignInAdmin member;
    private final TokenInfo token;


    public SignInAdminResponse(SignInAdmin member, TokenInfo token) {
        this.member = member;
        this.token = token;
    }

    @Getter
    public static class SignInAdmin {

        private final Long id;
        private final String email;

        public SignInAdmin(AdminInfo adminInfo) {
            this.id = adminInfo.id();
            this.email = adminInfo.email();
        }
    }
}
