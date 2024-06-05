package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.MemberInfo;
import lombok.Getter;

@Getter
public class CheckEmailResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final String phone;

    public CheckEmailResponse(Long id, String email, String name, String phone) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public CheckEmailResponse(MemberInfo member) {
        this.id = member.id();
        this.email = member.email();
        this.name = member.name();
        this.phone = member.phone();
    }
}
