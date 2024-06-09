package com.fc8.platform.dto.record;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.pinned.PinnedPostComment;
import com.fc8.platform.domain.entity.pinned.PinnedPostCommentImage;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Builder
public record CommentInfo(
    Long id,
    Long parentId,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String imageUrl,
    WriterInfo writer
) {
    public static CommentInfo fromEntity(PinnedPostComment comment, List<PinnedPostCommentImage> images, Admin admin, Member member) {
        // 기획 -> 이미지는 1장, 확장성을 고려하여 작성
        String imagePath = Optional.ofNullable(images)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0).getImagePath())
                .orElse(null);

        return CommentInfo.builder()
                .id(comment.getId())
                .parentId(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .imageUrl(imagePath)
                .writer(Optional.ofNullable(admin)
                        .map(WriterInfo::fromAdminEntity)
                        .orElseGet(() -> Optional.ofNullable(member)
                                .map(WriterInfo::fromMemberEntity)
                                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_COMMENT_WRITER))))
                .build();
    }

}