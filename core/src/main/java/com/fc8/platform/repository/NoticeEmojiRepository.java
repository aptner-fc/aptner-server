package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.notice.NoticeEmoji;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;

public interface NoticeEmojiRepository {
    EmojiCountInfo getEmojiCountInfoByNoticeAndMember(Notice notice);

    EmojiReactionInfo getEmojiReactionInfoByNoticeAndMember(Notice notice, Member member);

    boolean existsByNoticeAndMemberAndEmoji(Notice notice, Member member);

    NoticeEmoji store(NoticeEmoji noticeEmoji);

    NoticeEmoji getByNoticeAndMemberAndEmoji(Notice notice, Member member, EmojiType emoji);

    void delete(NoticeEmoji noticeEmoji);
}
