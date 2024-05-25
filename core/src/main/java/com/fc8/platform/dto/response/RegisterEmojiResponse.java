package com.fc8.platform.dto.response;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.EmojiInfo;
import lombok.Getter;

@Getter
public class RegisterEmojiResponse {

    private final Long emojiId;
    private final EmojiType emoji;

    public RegisterEmojiResponse(Long emojiId, EmojiType emoji) {
        this.emojiId = emojiId;
        this.emoji = emoji;
    }

    public RegisterEmojiResponse(EmojiInfo emojiInfo) {
        this.emojiId = emojiInfo.emojiId();
        this.emoji = emojiInfo.emoji();
    }
}
