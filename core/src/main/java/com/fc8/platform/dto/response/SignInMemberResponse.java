package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.MemberInfo;
import com.fc8.platform.dto.record.TokenInfo;
import lombok.Getter;

@Getter
public class SignInMemberResponse {

    private final SignInMember loginMember;
    private final TokenInfo token;


    public SignInMemberResponse(SignInMember loginMember, TokenInfo token) {
        this.loginMember = loginMember;
        this.token = token;
    }

    @Getter
    public static class SignInMember {

        private final Long id;
        private final String email;
        private final String nickname;

        public SignInMember(MemberInfo memberInfo) {
            this.id = memberInfo.id();
            this.email = memberInfo.email();
            this.nickname = memberInfo.nickname();
        }
    }
}
