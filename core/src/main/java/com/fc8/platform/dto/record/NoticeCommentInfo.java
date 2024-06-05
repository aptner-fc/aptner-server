package com.fc8.platform.dto.record;

import java.time.LocalDateTime;

public record NoticeCommentInfo(
    Long id,
    Long parentId,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String imageUrl,
    WriterInfo writer
) {
}
