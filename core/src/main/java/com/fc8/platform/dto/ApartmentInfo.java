package com.fc8.platform.dto;

import jakarta.validation.constraints.NotNull;

public record ApartmentInfo(@NotNull int dong, @NotNull int ho) {

}
