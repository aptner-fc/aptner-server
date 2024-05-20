package com.fc8.platform.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.enums.MemberStatus;
import com.fc8.platform.dto.record.AdminInfo;
import com.fc8.platform.dto.record.ApartInfo;
import com.fc8.platform.dto.record.MemberInfo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthMemberResponse {

    private final AuthMember member;
    private final AuthAdmin admin;
    private final ApartInfo apartInfo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime authenticatedAt;

    public AuthMemberResponse(AuthMember member, AuthAdmin admin, ApartInfo apartInfo, LocalDateTime authenticatedAt) {
        this.member = member;
        this.admin = admin;
        this.apartInfo = apartInfo;
        this.authenticatedAt = authenticatedAt;
    }

    @Getter
    public static class AuthMember {

        private final Long id;
        private final String email;
        private final String name;
        private final MemberStatus status;

        public AuthMember(MemberInfo member) {
            this.id = member.id();
            this.email = member.email();
            this.name = member.name();
            this.status = member.status();
        }
    }

    @Getter
    public static class AuthAdmin {

        private final Long id;
        private final String email;
        private final String name;

        public AuthAdmin(AdminInfo admin) {
            this.id = admin.id();
            this.email = admin.email();
            this.name = admin.name();
        }
    }
}
