package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.QnaComment;

import java.time.LocalDateTime;

public record QnaCommentInfo(
    Long id,
    Long parentId,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
//    String imageUrl,
    WriterInfo writer
) {
    public static QnaCommentInfo fromEntity(QnaComment comment, Member member) {
        return new QnaCommentInfo(
            comment.getId(),
            comment.getParent() == null ? null : comment.getParent().getId(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getUpdatedAt(),
//            comment.
            WriterInfo.fromMemberEntity(member)
        );
    }
}
