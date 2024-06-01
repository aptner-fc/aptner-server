package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostComment;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaComment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoadMyCommentInfo(
        Long id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ArticleSummary article
) {

    public static LoadMyCommentInfo fromPost(Post post, PostComment postComment, Category category) {
        return LoadMyCommentInfo.builder()
                .id(postComment.getId())
                .content(postComment.getContent())
                .createdAt(postComment.getCreatedAt())
                .updatedAt(postComment.getUpdatedAt())
                .article(ArticleSummary.fromPostEntity(post, category))
                .build();
    }

    public static LoadMyCommentInfo fromQna(Qna qna, QnaComment qnaComment, Category category) {
        return LoadMyCommentInfo.builder()
                .id(qnaComment.getId())
                .content(qnaComment.getContent())
                .createdAt(qnaComment.getCreatedAt())
                .updatedAt(qnaComment.getUpdatedAt())
                .article(ArticleSummary.fromQnaEntity(qna, category))
                .build();
    }

}
