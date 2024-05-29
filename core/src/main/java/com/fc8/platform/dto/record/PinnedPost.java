package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PinnedPost(Long id,
                         String title,
                         String content,
                         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
                         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
                         WriterInfo writer,
                         CategoryInfo category) {
}
