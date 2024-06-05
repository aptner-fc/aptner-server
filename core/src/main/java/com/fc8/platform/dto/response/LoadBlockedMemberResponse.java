package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.MemberSummary;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadBlockedMemberResponse {

    private final List<MemberSummary> blockedMemberList;

    public LoadBlockedMemberResponse(List<MemberSummary> blockedMemberList) {
        this.blockedMemberList = blockedMemberList;
    }
}
