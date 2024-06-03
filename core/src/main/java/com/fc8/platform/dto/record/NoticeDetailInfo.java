package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.notice.Notice;

import java.time.LocalDateTime;

public record NoticeDetailInfo(
    Long id,
    String title,
    String content,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime updatedAt,
    WriterInfo writer,
    CategoryInfo category,
    NoticeEmojiInfo emoji
) {
    public static NoticeDetailInfo fromEntity(
        Notice notice,
        Admin admin,
        Category category,
        EmojiCountInfo emojiCount,
        EmojiReactionInfo emojiReaction
    ) {
        return new NoticeDetailInfo(
            notice.getId(),
            notice.getTitle(),
            notice.getContent(),
            notice.getCreatedAt(),
            notice.getUpdatedAt(),
            WriterInfo.fromAdminEntity(admin),
            CategoryInfo.fromEntity(category),
            new NoticeEmojiInfo(emojiCount, emojiReaction)
        );
    }
}
