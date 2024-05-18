package com.fc8.platform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.enums.Gender;
import com.fc8.platform.domain.enums.MemberRole;
import com.fc8.platform.domain.enums.MemberStatus;
import com.fc8.platform.domain.enums.Provider;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberInfo(Long id,
                         String email,
                         String name,
                         String nickname,
                         String introduce,
                         String phone,
                         String profileImage,
                         Gender gender,
                         MemberStatus status,
                         MemberRole role,
                         Provider provider,
                         @JsonInclude(JsonInclude.Include.NON_NULL) LocalDateTime deletedAt) {

    public static MemberInfo fromEntity(Member member) {
        return MemberInfo.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .introduce(member.getIntroduce())
                .phone(member.getPhone())
                .profileImage(member.getProfileImage())
                .gender(member.getGender())
                .status(member.getStatus())
                .role(member.getRole())
                .provider(member.getProvider())
                .build();
    }

}
