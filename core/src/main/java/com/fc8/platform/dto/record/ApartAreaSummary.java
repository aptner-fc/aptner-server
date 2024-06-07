package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.apartment.ApartArea;
import lombok.Builder;

@Builder
public record ApartAreaSummary(
        Long id,
        Integer area,
        String imagePath
) {

    public static ApartAreaSummary fromEntity(ApartArea apartArea) {
        return ApartAreaSummary.builder()
                .id(apartArea.getId())
                .area(apartArea.getArea())
                .imagePath(apartArea.getImagePath())
                .build();
    }
}
