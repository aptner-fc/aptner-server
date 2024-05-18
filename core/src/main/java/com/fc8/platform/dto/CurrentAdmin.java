package com.fc8.platform.dto;

import com.fc8.platform.domain.entity.admin.Admin;
import lombok.Builder;

@Builder
public record CurrentAdmin(Long id,
                           String email,
                           ApartInfo apartInfo) {
    public static CurrentAdmin fromEntityWithApartInfo(Admin admin, ApartInfo apartInfo) {
        return CurrentAdmin.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .apartInfo(apartInfo)
                .build();
    }
}
