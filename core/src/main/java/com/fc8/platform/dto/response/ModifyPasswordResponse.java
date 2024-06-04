package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.MemberInfo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ModifyPasswordResponse {

    private final LocalDateTime updatedAt;

    public ModifyPasswordResponse(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ModifyPasswordResponse(MemberInfo memberInfo) {
        this.updatedAt = memberInfo.updatedAt();
    }
}
