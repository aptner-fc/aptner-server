package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.disclosure.Disclosure;

import java.time.LocalDateTime;

public record DisclosureInfo(
    Long id,
    String title,
    String content,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
    WriterInfo writer,
    CategoryInfo category
) {
    public static DisclosureInfo fromEntity(Disclosure disclosure, Admin admin, Category category) {
        return new DisclosureInfo(
            disclosure.getId(),
            disclosure.getTitle(),
            disclosure.getContent(),
            disclosure.getCreatedAt(),
            disclosure.getUpdatedAt(),
            WriterInfo.fromAdminEntity(admin),
            CategoryInfo.fromEntity(category)
        );
    }
}