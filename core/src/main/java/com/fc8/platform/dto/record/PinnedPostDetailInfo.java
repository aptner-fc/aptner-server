package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.pinned.PinnedPost;

import java.time.LocalDateTime;

public record PinnedPostDetailInfo(Long id,
                                   String title,
                                   String content,
                                   Long viewCount,
                                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
                                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
                                   WriterInfo writer,
                                   CategoryInfo category,
                                   PostEmojiInfo emoji) {

    public static PinnedPostDetailInfo fromEntityWithDomain(PinnedPost pinnedPost, Admin admin, Category category, EmojiCountInfo emojiCount, EmojiReactionInfo emojiReaction) {
        return new PinnedPostDetailInfo(
                pinnedPost.getId(),
                pinnedPost.getTitle(),
                pinnedPost.getContent(),
                pinnedPost.getViewCount(),
                pinnedPost.getCreatedAt(),
                pinnedPost.getUpdatedAt(),
                WriterInfo.fromAdminEntity(admin),
                CategoryInfo.fromEntity(category),
                new PostEmojiInfo(emojiCount, emojiReaction)
        );
    }

}
