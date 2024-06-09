package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.pinned.PinnedPost;

import java.time.LocalDateTime;

public record PinnedPostSummary(Long id,
                                String title,
                                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
                                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
                                WriterInfo writer,
                                CategoryInfo category) {

    public static PinnedPostSummary fromEntity(PinnedPost pinnedPost, Admin admin, Category category) {
        return new PinnedPostSummary(
                pinnedPost.getId(),
                pinnedPost.getTitle(),
                pinnedPost.getCreatedAt(),
                pinnedPost.getUpdatedAt(),
                WriterInfo.fromAdminEntity(admin),
                CategoryInfo.fromEntity(category)
        );
    }
}
