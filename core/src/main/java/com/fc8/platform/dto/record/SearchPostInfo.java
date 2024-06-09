package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.post.Post;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchPostInfo(
    Long id,
    String title,
    String content,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
    WriterInfo writer,
    CategoryInfo category,
    boolean isPinned
) {
    public static SearchPostInfo fromPost(Post post, Member member, Category category) {
        return new SearchPostInfo(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getCreatedAt(),
            post.getUpdatedAt(),
            WriterInfo.fromMemberEntity(member),
            CategoryInfo.fromEntity(category),
            false
        );
    }

    public static SearchPostInfo fromPinnedPost(PinnedPost pinnedPost, Admin admin, Category category) {
        return new SearchPostInfo(
            pinnedPost.getId(),
            pinnedPost.getTitle(),
            pinnedPost.getContent(),
            pinnedPost.getCreatedAt(),
            pinnedPost.getUpdatedAt(),
            WriterInfo.fromAdminEntity(admin),
            CategoryInfo.fromEntity(category),
            true
        );
    }
}
