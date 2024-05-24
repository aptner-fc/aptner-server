package com.fc8.platform.dto.record;

import java.time.LocalDateTime;

public record PinnedPost(Long id,
                         String title,
                         String content,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         WriterInfo writer,
                         CategoryInfo category) {
}
