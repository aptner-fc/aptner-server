package com.fc8.platform.common.s3;

import lombok.Builder;

@Builder
public record ImageUploadDto(
    String imageUrl,
    String message

) {
}