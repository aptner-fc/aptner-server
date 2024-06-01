package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.qna.Qna;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleSummary(
        Long id,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt,
        CategoryInfo category) {

        public static ArticleSummary fromPostEntity(Post post, Category category) {
                return ArticleSummary.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .createdAt(post.getCreatedAt())
                        .category(CategoryInfo.fromEntity(category))
                        .build();
        }

        public static ArticleSummary fromQnaEntity(Qna qna, Category category) {
                return ArticleSummary.builder()
                        .id(qna.getId())
                        .title(qna.getTitle())
                        .createdAt(qna.getCreatedAt())
                        .category(CategoryInfo.fromEntity(category))
                        .build();
        }
}
