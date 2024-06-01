package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.post.PostComment;

import java.time.LocalDateTime;

public record PostCommentInfo(
    Long id,
    Long parentId,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
//    String imageUrl,
    WriterInfo writer
) {
    public static PostCommentInfo fromEntity(PostComment comment, Member member) {
        return new PostCommentInfo(
                comment.getId(),
//            comment.getParent().getId(),
                comment.getParent() == null ? null : comment.getParent().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
//            comment.
                WriterInfo.fromMemberEntity(member)
        );
    }

}
