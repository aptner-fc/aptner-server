package com.fc8.platform.dto.record;

import lombok.Builder;

@Builder
public record UploadImageInfo(
    String originalImageUrl

) {
}