package com.fc8.platform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ApartmentInfo(@NotNull String code, @NotNull ApartDetailInfo apartDetailInfo) {
}
