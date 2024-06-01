package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.qna.QnaFile;

public record QnaFileInfo(
    Long id,
    String name,
    String path,
    Long size
) {
    public static QnaFileInfo fromEntity(QnaFile qnaFile) {
        return new QnaFileInfo(
            qnaFile.getId(),
            qnaFile.getName(),
            qnaFile.getPath(),
            qnaFile.getSize()
        );
    }
}
