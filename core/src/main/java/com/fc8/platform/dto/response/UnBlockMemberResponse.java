package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.MemberInfo;
import lombok.Getter;

@Getter
public class UnBlockMemberResponse {

    private final Long unBlockedMemberId;

    public UnBlockMemberResponse(Long unBlockedMemberId) {
        this.unBlockedMemberId = unBlockedMemberId;
    }

    public UnBlockMemberResponse(MemberInfo unBlockedMember) {
        this.unBlockedMemberId = unBlockedMember.id();
    }
}
