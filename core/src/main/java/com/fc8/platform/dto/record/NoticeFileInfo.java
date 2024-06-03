package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.notice.NoticeFile;

public record NoticeFileInfo(
    Long id,
    String name,
    String path,
    Long size
) {
    public static NoticeFileInfo fromEntity(NoticeFile noticeFile) {
        return new NoticeFileInfo(
            noticeFile.getId(),
            noticeFile.getName(),
            noticeFile.getPath(),
            noticeFile.getSize()
        );
    }
}
