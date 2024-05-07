package com.fc8.aptner.common.s3;

import lombok.Builder;

@Builder
public record ImageUploadDto(
    String imageUrl,
    String message

) {
}