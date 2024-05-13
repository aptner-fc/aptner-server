package com.fc8.aptner.core.dto;

import jakarta.validation.constraints.NotNull;

public record SignInMemberInfo(@NotNull MemberInfo memberInfo,
                               @NotNull TokenInfo tokenInfo) {
}
