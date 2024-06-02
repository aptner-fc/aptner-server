package com.fc8.platform.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.dto.record.MemberSummary;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChangePasswordResponse {

    private final Long id;
    private final String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime updatedAt;

    public ChangePasswordResponse(Long id, String email, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.updatedAt = updatedAt;
    }

    public ChangePasswordResponse(MemberSummary member) {
        this.id = member.id();
        this.email = member.email();
        this.updatedAt = member.updatedAt();
    }
}
