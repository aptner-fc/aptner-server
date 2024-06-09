package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.pinned.PinnedPost;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchNoticeInfo(
    Long id,
    String title,
    String content,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
    WriterInfo writer,
    CategoryInfo category,
    boolean isPinned
) {
    public static SearchNoticeInfo fromNotice(Notice notice, Admin admin, Category category) {
        return new SearchNoticeInfo(
            notice.getId(),
            notice.getTitle(),
            notice.getContent(),
            notice.getCreatedAt(),
            notice.getUpdatedAt(),
            WriterInfo.fromAdminEntity(admin),
            CategoryInfo.fromEntity(category),
            false
        );
    }

    public static SearchNoticeInfo fromPinnedNotice(PinnedPost pinnedNotice, Admin admin, Category category) {
        return new SearchNoticeInfo(
            pinnedNotice.getId(),
            pinnedNotice.getTitle(),
            pinnedNotice.getContent(),
            pinnedNotice.getCreatedAt(),
            pinnedNotice.getUpdatedAt(),
            WriterInfo.fromAdminEntity(admin),
            CategoryInfo.fromEntity(category),
            true
        );
    }

}
