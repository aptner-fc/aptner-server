package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostEmoji;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;

public interface PostEmojiRepository {

    PostEmoji store(PostEmoji postEmoji);

    boolean existsByPostAndMemberAndEmoji(Post post, Member member);

    PostEmoji getByPostAndMemberAndEmoji(Post post, Member member, EmojiType emoji);

    void delete(PostEmoji postEmoji);

    EmojiCountInfo getEmojiCountInfoByPostAndMember(Post post);

    EmojiReactionInfo getEmojiReactionInfoByPostAndMember(Post post, Member member);
}
