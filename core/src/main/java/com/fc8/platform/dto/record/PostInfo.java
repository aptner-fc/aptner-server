package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.post.Post;

import java.time.LocalDateTime;

public record PostInfo(Long id,
                       String title,
                       String content,
                       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
                       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
                       WriterInfo writer,
                       CategoryInfo category) {

    public static PostInfo fromEntity(Post post, Member member, Category category) {
        return new PostInfo(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                WriterInfo.fromMemberEntity(member),
                CategoryInfo.fromEntity(category)
        );
    }

}
