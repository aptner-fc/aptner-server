package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;

public interface NoticeEmojiRepository {
    EmojiCountInfo getEmojiCountInfoByNoticeAndMember(Notice notice);

    EmojiReactionInfo getEmojiReactionInfoByNoticeAndMember(Notice notice, Member member);
}
