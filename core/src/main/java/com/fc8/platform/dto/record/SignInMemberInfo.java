package com.fc8.platform.dto.record;

import jakarta.validation.constraints.NotNull;

public record SignInMemberInfo(@NotNull MemberInfo memberInfo,
                               @NotNull TokenInfo tokenInfo) {
}
