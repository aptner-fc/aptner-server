package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.notice.Notice;

import java.time.LocalDateTime;

public record NoticeInfo(
    Long id,
    String title,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
    WriterInfo writer,
    CategoryInfo category,
    Long commentCount,
    boolean isFileAttached
) {
    public static NoticeInfo fromEntity(Notice notice, Admin admin, Category category, Long commentCount, boolean isFileAttached) {
        return new NoticeInfo(
            notice.getId(),
            notice.getTitle(),
            notice.getCreatedAt(),
            notice.getUpdatedAt(),
            WriterInfo.fromAdminEntity(admin),
            CategoryInfo.fromEntity(category),
            commentCount,
            isFileAttached
        );
    }
}
