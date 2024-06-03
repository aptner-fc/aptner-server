package com.fc8.platform.dto.record;

import java.time.LocalDateTime;

public record DeletedCountInfo(
        int deletedCount,
        LocalDateTime deletedAt
) {

    public static DeletedCountInfo notDeleted() {
        return new DeletedCountInfo(0, null);
    }
}
