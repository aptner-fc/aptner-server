package com.fc8.platform.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.Qna;

import java.time.LocalDateTime;

public record QnaDetailInfo(
    Long id,
    String title,
    String content,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime updatedAt,
    boolean isPrivate,
    WriterInfo writer,
    CategoryInfo category,
    QnaEmojiInfo emoji
) {
    public static QnaDetailInfo fromEntity(
        Qna qna,
        Member member,
        Category category,
        EmojiCountInfo emojiCount,
        EmojiReactionInfo emojiReaction
    ) {
        return new QnaDetailInfo(
            qna.getId(),
            qna.getTitle(),
            qna.getContent(),
            qna.getCreatedAt(),
            qna.getUpdatedAt(),
            qna.isPrivate(),
            WriterInfo.fromMemberEntity(member),
            CategoryInfo.fromEntity(category),
            new QnaEmojiInfo(emojiCount, emojiReaction)
        );
    }
}
