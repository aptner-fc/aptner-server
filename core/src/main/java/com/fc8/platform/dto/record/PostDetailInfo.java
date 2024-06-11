package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.apartment.ApartArea;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.post.Post;

import java.time.LocalDateTime;
import java.util.Optional;

public record PostDetailInfo(Long id,
                             String title,
                             String content,
                             Long viewCount,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime createdAt,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime updatedAt,
                             WriterInfo writer,
                             CategoryInfo category,
                             PostEmojiInfo emoji,
                             ApartAreaSummary apartArea) {

    public static PostDetailInfo fromEntityWithDomain(Post post, Member member, Category category, ApartArea apartArea,  EmojiCountInfo emojiCount, EmojiReactionInfo emojiReaction) {
        return new PostDetailInfo(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                WriterInfo.fromMemberEntity(member),
                CategoryInfo.fromEntity(category),
                new PostEmojiInfo(emojiCount, emojiReaction),
                Optional.ofNullable(apartArea)
                        .map(ApartAreaSummary::fromEntity)
                        .orElse(null)
        );
    }

}
