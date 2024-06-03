package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.Qna;

import java.time.LocalDateTime;

public record QnaInfo(
    Long id,
    String title,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
    WriterInfo writer,
    CategoryInfo category,
    boolean isPrivate
) {
    public static QnaInfo fromEntity(Qna qna, Member member, Category category) {
        return new QnaInfo(
            qna.getId(),
            qna.getTitle(),
            qna.getCreatedAt(),
            qna.getUpdatedAt(),
            WriterInfo.fromMemberEntity(member),
            CategoryInfo.fromEntity(category),
            qna.isPrivate()
        );
    }
}
