package com.fc8.aptner.core.dto;

import jakarta.validation.constraints.NotNull;

public record ApartmentInfo(@NotNull int dong, @NotNull int ho) {

}
