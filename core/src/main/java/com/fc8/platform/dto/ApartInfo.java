package com.fc8.platform.dto;

import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.enums.ApartType;
import lombok.Builder;

@Builder
public record ApartInfo(Long id,
                        String code,
                        String title,
                        ApartType type) {
    public static ApartInfo fromEntity(Apart apart) {
        return ApartInfo.builder()
                .id(apart.getId())
                .code(apart.getCode())
                .title(apart.getTitle())
                .type(apart.getType())
                .build();
    }

}
