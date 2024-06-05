package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.MemberInfo;
import lombok.Getter;

@Getter
public class BlockMemberResponse {

    private final Long blockedMemberId;

    public BlockMemberResponse(Long blockedMemberId) {
        this.blockedMemberId = blockedMemberId;
    }

    public BlockMemberResponse(MemberInfo blockedMember) {
        this.blockedMemberId = blockedMember.id();
    }
}
