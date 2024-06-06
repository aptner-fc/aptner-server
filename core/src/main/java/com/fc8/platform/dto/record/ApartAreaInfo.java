package com.fc8.platform.dto.record;

public record ApartAreaInfo(
        Long id,
        Integer area,
        boolean isUsed,
        String imagePath,
        ApartInfo apart
) {
}
