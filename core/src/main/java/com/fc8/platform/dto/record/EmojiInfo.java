package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.disclosure.DisclosureEmoji;
import com.fc8.platform.domain.entity.notice.NoticeEmoji;
import com.fc8.platform.domain.entity.pinned.PinnedPostEmoji;
import com.fc8.platform.domain.entity.post.PostEmoji;
import com.fc8.platform.domain.entity.qna.QnaEmoji;
import com.fc8.platform.domain.enums.EmojiType;

public record EmojiInfo(
    Long emojiId,
    EmojiType emoji
) {
    public static EmojiInfo fromPostEmojiEntity(PostEmoji postEmoji) {
        return new EmojiInfo(postEmoji.getId(), postEmoji.getEmoji());
    }

    public static EmojiInfo fromQnaEmojiEntity(QnaEmoji qnaEmoji) {
        return new EmojiInfo(qnaEmoji.getId(), qnaEmoji.getEmoji());
    }

    public static EmojiInfo fromNoticeEmojiEntity(NoticeEmoji noticeEmoji) {
        return new EmojiInfo(noticeEmoji.getId(), noticeEmoji.getEmoji());
    }

    public static EmojiInfo fromDisclosureEmojiEntity(DisclosureEmoji disclosureEmoji) {
        return new EmojiInfo(disclosureEmoji.getId(), disclosureEmoji.getEmoji());
    }

    public static EmojiInfo fromPinnedPostEmojiEntity(PinnedPostEmoji pinnedPostEmoji) {
        return new EmojiInfo(pinnedPostEmoji.getId(), pinnedPostEmoji.getEmoji());
    }
}
