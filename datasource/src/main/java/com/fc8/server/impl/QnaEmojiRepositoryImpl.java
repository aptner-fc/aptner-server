package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.qna.QQna;
import com.fc8.platform.domain.entity.qna.QQnaEmoji;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaEmoji;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;
import com.fc8.platform.repository.QnaEmojiRepository;
import com.fc8.server.QnaEmojiJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QnaEmojiRepositoryImpl implements QnaEmojiRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QnaEmojiJpaRepository qnaEmojiJpaRepository;

    QQnaEmoji qnaEmoji = QQnaEmoji.qnaEmoji;
    QQna qna = QQna.qna;
    QMember member = QMember.member;

    @Override
    public QnaEmoji store(QnaEmoji qnaEmoji) {
        return qnaEmojiJpaRepository.save(qnaEmoji);
    }

    @Override
    public boolean existsByQnaAndMemberAndEmoji(Qna activeQna, Member loginMember) {
        return jpaQueryFactory
            .selectOne()
            .from(qnaEmoji)
            .innerJoin(qna).on(eqQna(qnaEmoji, activeQna))
            .innerJoin(member).on(eqMember(qnaEmoji, loginMember))
            .where(
                isNotDeleted(qna)
            )
            .fetchFirst() != null;
    }

    @Override
    public QnaEmoji getByQnaAndMemberAndEmoji(Qna activeQna, Member loginMember, EmojiType emoji) {
        QnaEmoji addedEmoji = jpaQueryFactory
            .selectFrom(qnaEmoji)
            .innerJoin(qna).on(eqQna(qnaEmoji, activeQna))
            .innerJoin(member).on(eqMember(qnaEmoji, loginMember))
            .where(
                isNotDeleted(qna),
                eqEmojiType(qnaEmoji, emoji)
            )
            .fetchOne();

        return Optional.ofNullable(addedEmoji)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_EMOJI));
    }

    @Override
    public void delete(QnaEmoji qnaEmoji) {
        qnaEmojiJpaRepository.delete(qnaEmoji);
    }

    @Override
    public EmojiCountInfo getEmojiCountInfoByQnaAndMember(Qna qna) {
        long likeCount = countEmojiByType(qna, EmojiType.LIKE);
        long empathyCount = countEmojiByType(qna, EmojiType.EMPATHY);
        long funCount = countEmojiByType(qna, EmojiType.FUN);
        long amazingCount = countEmojiByType(qna, EmojiType.AMAZING);
        long sadCount = countEmojiByType(qna, EmojiType.SAD);

        return new EmojiCountInfo(likeCount, empathyCount, funCount, amazingCount, sadCount);
    }

    @Override
    public EmojiReactionInfo getEmojiReactionInfoByQnaAndMember(Qna qna, Member member) {
        boolean reactedLike = reactedEmoji(qna, member, EmojiType.LIKE);
        boolean reactedEmpathy = reactedEmoji(qna, member, EmojiType.EMPATHY);
        boolean reactedFun = reactedEmoji(qna, member, EmojiType.FUN);
        boolean reactedAmazing = reactedEmoji(qna, member, EmojiType.AMAZING);
        boolean reactedSad = reactedEmoji(qna, member, EmojiType.SAD);

        return new EmojiReactionInfo(reactedLike, reactedEmpathy, reactedFun, reactedAmazing, reactedSad);
    }

    private boolean reactedEmoji(Qna qna, Member member, EmojiType emojiType) {
        return jpaQueryFactory
            .selectOne()
            .from(qnaEmoji)
            .where(
                eqQna(qnaEmoji, qna),
                eqMember(qnaEmoji, member),
                eqEmojiType(qnaEmoji, emojiType)
            )
            .fetchFirst() != null;
    }

    private BooleanExpression eqMember(QQnaEmoji qnaEmoji, Member member) {
        return qnaEmoji.member.eq(member);
    }

    private BooleanExpression eqQna(QQnaEmoji qnaEmoji, Qna qna) {
        return qnaEmoji.qna.eq(qna);
    }

    private long countEmojiByType(Qna qna, EmojiType emojiType) {
        Long count = jpaQueryFactory
            .select(qnaEmoji.count())
            .from(qnaEmoji)
            .where(
                eqQna(qnaEmoji, qna),
                eqEmojiType(qnaEmoji, emojiType)
            )
            .fetchOne();

        return Optional.ofNullable(count).orElse(0L);
    }

    private BooleanExpression isNotDeleted(QQna qna) {
        return qna.deletedAt.isNull();
    }

    private BooleanExpression eqEmojiType(QQnaEmoji qnaEmoji, EmojiType emoji) {
        return qnaEmoji.emoji.eq(emoji);
    }

}
