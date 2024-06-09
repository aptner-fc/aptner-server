package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.pinned.PinnedPost;

import java.time.LocalDateTime;

public record PinnedPostQueryInfo(
    Long id,
    String title,
    String content,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
    WriterInfo writer,
    CategoryInfo category
) {
    public static PinnedPostQueryInfo fromEntity(PinnedPost pinnedPost, Member member, Category category) {
        return new PinnedPostQueryInfo(
            pinnedPost.getId(),
            pinnedPost.getTitle(),
            pinnedPost.getContent(),
            pinnedPost.getCreatedAt(),
            pinnedPost.getUpdatedAt(),
            WriterInfo.fromMemberEntity(member),
            CategoryInfo.fromEntity(category)
        );
    }
}
