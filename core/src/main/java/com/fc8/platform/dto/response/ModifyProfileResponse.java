package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.MemberSummary;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ModifyProfileResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String profileImage;
    private final LocalDateTime updatedAt;

    public ModifyProfileResponse(Long id, String email, String nickname, String profileImage, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.updatedAt = updatedAt;
    }

    public ModifyProfileResponse(MemberSummary member) {
        this.id = member.id();
        this.email = member.email();
        this.nickname = member.nickname();
        this.profileImage = member.profileImage();
        this.updatedAt = member.updatedAt();
    }
}
