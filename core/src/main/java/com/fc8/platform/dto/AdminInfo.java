package com.fc8.platform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.enums.Gender;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminInfo(Long id,
                        String email,
                        String name,
                        String nickname,
                        String introduce,
                        String phone,
                        String profileImage,
                        Gender gender,
                        @JsonInclude(JsonInclude.Include.NON_NULL) LocalDateTime deletedAt) {

    public static AdminInfo fromEntity(Admin admin) {
        return AdminInfo.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .name(admin.getName())
                .nickname(admin.getNickname())
                .introduce(admin.getIntroduce())
                .phone(admin.getPhone())
                .profileImage(admin.getProfileImage())
                .gender(admin.getGender())
                .build();
    }

}
