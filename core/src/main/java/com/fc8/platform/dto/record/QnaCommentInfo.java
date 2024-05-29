package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.QnaComment;

import java.time.LocalDateTime;

public record QnaCommentInfo(
    Long id,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    WriterInfo writer
) {
    public static QnaCommentInfo fromEntity(QnaComment comment, Member member) {
        return new QnaCommentInfo(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getUpdatedAt(),
            WriterInfo.fromMemberEntity(member)
        );
    }
}
