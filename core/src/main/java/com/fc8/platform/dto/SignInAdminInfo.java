package com.fc8.platform.dto;

import jakarta.validation.constraints.NotNull;

public record SignInAdminInfo(@NotNull AdminInfo adminInfo,
                              @NotNull TokenInfo tokenInfo) {
}
