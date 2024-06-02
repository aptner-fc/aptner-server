package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.member.Member;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberSummary(Long id,
                            String email,
                            String name,
                            String nickname,
                            String phone,
                            String profileImage,
                            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                            LocalDateTime createdAt,
                            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
                            LocalDateTime updatedAt) {

    public static MemberSummary fromEntity(Member member) {
        return MemberSummary.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .profileImage(member.getProfileImage())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

}
