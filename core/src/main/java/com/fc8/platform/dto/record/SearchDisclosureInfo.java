package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.pinned.PinnedPost;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchDisclosureInfo(
    Long id,
    String title,
    String content,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
    WriterInfo writer,
    CategoryInfo category,
    boolean isPinned
) {
    public static SearchDisclosureInfo fromDisclosure(Disclosure disclosure, Admin admin, Category category) {
        return new SearchDisclosureInfo(
            disclosure.getId(),
            disclosure.getTitle(),
            disclosure.getContent(),
            disclosure.getCreatedAt(),
            disclosure.getUpdatedAt(),
            WriterInfo.fromAdminEntity(admin),
            CategoryInfo.fromEntity(category),
            false
        );
    }

    public static SearchDisclosureInfo fromPinnedDisclosure(PinnedPost pinnedDisclosure, Admin admin, Category category) {
        return new SearchDisclosureInfo(
            pinnedDisclosure.getId(),
            pinnedDisclosure.getTitle(),
            pinnedDisclosure.getContent(),
            pinnedDisclosure.getCreatedAt(),
            pinnedDisclosure.getUpdatedAt(),
            WriterInfo.fromAdminEntity(admin),
            CategoryInfo.fromEntity(category),
            true
        );
    }

}
