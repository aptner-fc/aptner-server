package com.fc8.platform.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.dto.record.MemberSummary;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChangePhoneResponse {

    private final Long id;
    private final String email;
    private final String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime updatedAt;

    public ChangePhoneResponse(Long id, String email, String phone, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.updatedAt = updatedAt;
    }

    public ChangePhoneResponse(MemberSummary member) {
        this.id = member.id();
        this.email = member.email();
        this.phone = member.phone();
        this.updatedAt = member.updatedAt();
    }
}
