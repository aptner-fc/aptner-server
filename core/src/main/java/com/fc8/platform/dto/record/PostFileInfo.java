package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.post.PostFile;

public record PostFileInfo(
    Long id,
    String name,
    String path,
    Long size
) {
    public static PostFileInfo fromEntity(PostFile postFile) {
        return new PostFileInfo(
            postFile.getId(),
            postFile.getName(),
            postFile.getPath(),
            postFile.getSize()
        );
    }
}
