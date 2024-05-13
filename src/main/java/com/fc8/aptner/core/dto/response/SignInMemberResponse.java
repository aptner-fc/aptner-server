package com.fc8.aptner.core.dto.response;

import com.fc8.aptner.core.domain.enums.MemberStatus;
import com.fc8.aptner.core.dto.MemberInfo;
import com.fc8.aptner.core.dto.TokenInfo;
import lombok.Getter;

@Getter
public class SignInMemberResponse {

    private final SignInMember member;
    private final TokenInfo token;


    public SignInMemberResponse(SignInMember member, TokenInfo token) {
        this.member = member;
        this.token = token;
    }

    @Getter
    public static class SignInMember {

        private final Long id;
        private final String email;
        private final MemberStatus status;

        public SignInMember(MemberInfo memberInfo) {
            this.id = memberInfo.id();
            this.email = memberInfo.email();
            this.status = memberInfo.status();
        }
    }
}
