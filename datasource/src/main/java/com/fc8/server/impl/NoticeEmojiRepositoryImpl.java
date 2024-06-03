package com.fc8.server.impl;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.notice.QNotice;
import com.fc8.platform.domain.entity.notice.QNoticeEmoji;
import com.fc8.platform.domain.entity.qna.QQnaEmoji;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;
import com.fc8.platform.repository.NoticeEmojiRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeEmojiRepositoryImpl implements NoticeEmojiRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QNoticeEmoji noticeEmoji = QNoticeEmoji.noticeEmoji;
    QNotice notice = QNotice.notice;
    QMember member = QMember.member;

    @Override
    public EmojiCountInfo getEmojiCountInfoByNoticeAndMember(Notice notice) {
        long likeCount = countEmojiByType(notice, EmojiType.LIKE);
        long empathyCount = countEmojiByType(notice, EmojiType.EMPATHY);
        long funCount = countEmojiByType(notice, EmojiType.FUN);
        long amazingCount = countEmojiByType(notice, EmojiType.AMAZING);
        long sadCount = countEmojiByType(notice, EmojiType.SAD);

        return new EmojiCountInfo(likeCount, empathyCount, funCount, amazingCount, sadCount);
    }

    @Override
    public EmojiReactionInfo getEmojiReactionInfoByNoticeAndMember(Notice notice, Member member) {
        boolean reactedLike = reactedEmoji(notice, member, EmojiType.LIKE);
        boolean reactedEmpathy = reactedEmoji(notice, member, EmojiType.EMPATHY);
        boolean reactedFun = reactedEmoji(notice, member, EmojiType.FUN);
        boolean reactedAmazing = reactedEmoji(notice, member, EmojiType.AMAZING);
        boolean reactedSad = reactedEmoji(notice, member, EmojiType.SAD);

        return new EmojiReactionInfo(reactedLike, reactedEmpathy, reactedFun, reactedAmazing, reactedSad);
    }

    private boolean reactedEmoji(Notice notice, Member member, EmojiType emojiType) {
        return jpaQueryFactory
            .selectOne()
            .from(noticeEmoji)
            .where(
                eqNotice(noticeEmoji, notice),
                eqMember(noticeEmoji, member),
                eqEmojiType(noticeEmoji, emojiType)
            )
            .fetchFirst() != null;
    }

    private long countEmojiByType(Notice notice, EmojiType emojiType) {
        Long count = jpaQueryFactory
            .select(noticeEmoji.count())
            .from(noticeEmoji)
            .where(
                eqNotice(noticeEmoji, notice),
                eqEmojiType(noticeEmoji, emojiType)
            )
            .fetchOne();

        return Optional.ofNullable(count).orElse(0L);
    }

    private BooleanExpression eqNotice(QNoticeEmoji noticeEmoji, Notice notice) {
        return noticeEmoji.notice.eq(notice);
    }

    private BooleanExpression eqMember(QNoticeEmoji noticeEmoji, Member member) {
        return noticeEmoji.member.eq(member);
    }

    private BooleanExpression eqEmojiType(QNoticeEmoji noticeEmoji, EmojiType emoji) {
        return noticeEmoji.emoji.eq(emoji);
    }
}
