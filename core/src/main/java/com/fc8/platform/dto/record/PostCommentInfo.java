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
    String imageUrl,
    WriterInfo writer
) {
}
