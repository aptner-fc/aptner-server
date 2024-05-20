package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.enums.ApartType;
import lombok.Builder;

import java.util.List;

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

    public static List<ApartInfo> fromEntityList(List<Apart> apartList) {
        return apartList
                .stream()
                .map(ApartInfo::fromEntity)
                .toList();
    }

}
