package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaEmoji;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;

public interface QnaEmojiRepository {

    QnaEmoji store(QnaEmoji qnaEmoji);

    boolean existsByQnaAndMemberAndEmoji(Qna qna, Member member);

    QnaEmoji getByQnaAndMemberAndEmoji(Qna qna, Member member, EmojiType emoji);

    void delete(QnaEmoji qnaEmoji);

    EmojiCountInfo getEmojiCountInfoByQnaAndMember(Qna qna);

    EmojiReactionInfo getEmojiReactionInfoByQnaAndMember(Qna qna, Member member);

}
