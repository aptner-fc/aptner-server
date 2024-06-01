package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.qna.Qna;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoadMyArticleInfo(
        Long id,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        CategoryInfo category
) {

    public static LoadMyArticleInfo fromPost(Post post, Category category) {
        return LoadMyArticleInfo.builder()
                .id(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .category(CategoryInfo.fromEntity(category))
                .build();
    }

    public static LoadMyArticleInfo fromQna(Qna qna, Category category) {
        return LoadMyArticleInfo.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .createdAt(qna.getCreatedAt())
                .updatedAt(qna.getUpdatedAt())
                .category(CategoryInfo.fromEntity(category))
                .build();
    }

}
