package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.MemberInfo;
import lombok.Getter;

@Getter
public class FindEmailResponse {

    private final String email;

    public FindEmailResponse(String email) {
        this.email = email;
    }

    public FindEmailResponse(MemberInfo memberInfo) {
        this.email = memberInfo.email();
    }
}
