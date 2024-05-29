package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.Qna;

import java.time.LocalDateTime;

public record QnaInfo(
    Long id,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    WriterInfo writer,
    CategoryInfo category,
    Boolean isPrivate
) {
    public static QnaInfo fromEntity(Qna qna, Member member, Category category) {
        return new QnaInfo(
            qna.getId(),
            qna.getTitle(),
            qna.getContent(),
            qna.getCreatedAt(),
            qna.getUpdatedAt(),
            WriterInfo.fromMemberEntity(member),
            CategoryInfo.fromEntity(category),
            qna.getIsPrivate()
        );
    }
}
