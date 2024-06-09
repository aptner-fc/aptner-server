package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.qna.Qna;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchQnaInfo(
    Long id,
    String title,
    String content,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
    WriterInfo writer,
    CategoryInfo category,
    boolean isPrivate,
    boolean isPinned
) {
    public static SearchQnaInfo fromQna(Qna qna, Member member, Category category) {
        return new SearchQnaInfo(
            qna.getId(),
            qna.getTitle(),
            qna.getContent(),
            qna.getCreatedAt(),
            qna.getUpdatedAt(),
            WriterInfo.fromMemberEntity(member),
            CategoryInfo.fromEntity(category),
            qna.isPrivate(),
            false
        );
    }

    public static SearchQnaInfo fromPinnedQna(PinnedPost pinnedQna, Admin admin, Category category) {
        return new SearchQnaInfo(
            pinnedQna.getId(),
            pinnedQna.getTitle(),
            pinnedQna.getContent(),
            pinnedQna.getCreatedAt(),
            pinnedQna.getUpdatedAt(),
            WriterInfo.fromAdminEntity(admin),
            CategoryInfo.fromEntity(category),
            false,
            true
        );
    }

}
