package com.fc8.platform.dto.record;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AuthMemberInfo(Long id,
                             MemberInfo memberInfo,
                             AdminInfo adminInfo,
                             ApartInfo apartInfo,
                             LocalDateTime authenticatedAt) {

}
