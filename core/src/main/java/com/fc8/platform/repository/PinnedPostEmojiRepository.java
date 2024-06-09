package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.pinned.PinnedPostEmoji;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;

public interface PinnedPostEmojiRepository {

    PinnedPostEmoji store(PinnedPostEmoji pinnedPostEmoji);

    EmojiCountInfo getEmojiCountInfoByPost(PinnedPost pinnedPost);

    EmojiReactionInfo getEmojiReactionInfoByPostAndMember(PinnedPost pinnedPost, Member member);

    boolean existsByPinnedPostAndMemberAndEmoji(PinnedPost pinnedPost, Member member, EmojiType emoji);

    PinnedPostEmoji getByPinnedPostAndMemberAndEmoji(PinnedPost pinnedPost, Member member, EmojiType emoji);

    void delete(PinnedPostEmoji pinnedPostEmoji);
}
