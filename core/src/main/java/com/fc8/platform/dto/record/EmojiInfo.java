package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.post.PostEmoji;
import com.fc8.platform.domain.enums.EmojiType;

public record EmojiInfo(Long emojiId,
                        EmojiType emoji) {

    public static EmojiInfo fromPostEmojiEntity(PostEmoji postEmoji) {
        return new EmojiInfo(postEmoji.getId(), postEmoji.getEmoji());
    }
}
