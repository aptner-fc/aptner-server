package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.member.Member;

public record WriterInfo(Long id,
                         String name,
                         String nickname) {

    public static WriterInfo fromMemberEntity(Member member) {
        return new WriterInfo(member.getId(), member.getName(), member.getNickname());
    }

    public static WriterInfo fromAdminEntity(Admin admin) {
        return new WriterInfo(admin.getId(), admin.getName(), admin.getNickname());
    }

}
