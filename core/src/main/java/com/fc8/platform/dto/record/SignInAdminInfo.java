package com.fc8.platform.dto.record;

import jakarta.validation.constraints.NotNull;

public record SignInAdminInfo(@NotNull AdminInfo adminInfo,
                              @NotNull TokenInfo tokenInfo) {
}
