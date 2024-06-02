package com.fc8.platform.dto.record;

import lombok.Builder;

@Builder
public record UploadFileInfo(
        Long fileSize,
        String fileName,
        String fileExtension,
        String originalFileName,
        String originalFilUrl) {

}