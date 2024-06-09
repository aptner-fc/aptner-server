package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.disclosure.DisclosureEmoji;
import com.fc8.platform.domain.entity.disclosure.QDisclosure;
import com.fc8.platform.domain.entity.disclosure.QDisclosureEmoji;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;
import com.fc8.platform.repository.DisclosureEmojiRepository;
import com.fc8.server.DisclosureEmojiJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DisclosureEmojiRepositoryImpl implements DisclosureEmojiRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final DisclosureEmojiJpaRepository disclosureEmojiJpaRepository;

    QDisclosureEmoji disclosureEmoji = QDisclosureEmoji.disclosureEmoji;
    QDisclosure disclosure = QDisclosure.disclosure;
    QMember member = QMember.member;

    @Override
    public EmojiCountInfo getEmojiCountInfoByDisclosureAndMember(Disclosure disclosure) {
        long likeCount = countEmojiByType(disclosure, EmojiType.LIKE);
        long empathyCount = countEmojiByType(disclosure, EmojiType.EMPATHY);
        long funCount = countEmojiByType(disclosure, EmojiType.FUN);
        long amazingCount = countEmojiByType(disclosure, EmojiType.AMAZING);
        long sadCount = countEmojiByType(disclosure, EmojiType.SAD);

        return new EmojiCountInfo(likeCount, empathyCount, funCount, amazingCount, sadCount);
    }

    @Override
    public EmojiReactionInfo getEmojiReactionInfoByDisclosureAndMember(Disclosure disclosure, Member member) {
        boolean reactedLike = reactedEmoji(disclosure, member, EmojiType.LIKE);
        boolean reactedEmpathy = reactedEmoji(disclosure, member, EmojiType.EMPATHY);
        boolean reactedFun = reactedEmoji(disclosure, member, EmojiType.FUN);
        boolean reactedAmazing = reactedEmoji(disclosure, member, EmojiType.AMAZING);
        boolean reactedSad = reactedEmoji(disclosure, member, EmojiType.SAD);

        return new EmojiReactionInfo(reactedLike, reactedEmpathy, reactedFun, reactedAmazing, reactedSad);
    }

    @Override
    public boolean existsByDisclosureAndMemberAndEmoji(Disclosure activeDisclosure, Member loginMember) {
        return jpaQueryFactory
            .selectOne()
            .from(disclosureEmoji)
            .innerJoin(disclosure).on(eqDisclosure(disclosureEmoji, activeDisclosure))
            .innerJoin(member).on(eqMember(disclosureEmoji, loginMember))
            .where(
                isNotDeleted(disclosure)
            )
            .fetchFirst() != null;
    }

    @Override
    public DisclosureEmoji store(DisclosureEmoji disclosureEmoji) {
        return disclosureEmojiJpaRepository.save(disclosureEmoji);
    }

    @Override
    public DisclosureEmoji getByDisclosureAndMemberAndEmoji(Disclosure activeDisclosure, Member loginMember, EmojiType emoji) {
        DisclosureEmoji addedEmoji = jpaQueryFactory
            .selectFrom(disclosureEmoji)
            .innerJoin(disclosure).on(eqDisclosure(disclosureEmoji, activeDisclosure))
            .innerJoin(member).on(eqMember(disclosureEmoji, loginMember))
            .where(
                isNotDeleted(disclosure),
                eqEmojiType(disclosureEmoji, emoji)
            )
            .fetchOne();

        return Optional.ofNullable(addedEmoji)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_EMOJI));
    }

    @Override
    public void delete(DisclosureEmoji disclosureEmoji) {
        disclosureEmojiJpaRepository.delete(disclosureEmoji);
    }

    private boolean reactedEmoji(Disclosure disclosure, Member member, EmojiType emojiType) {
        return jpaQueryFactory
            .selectOne()
            .from(disclosureEmoji)
            .where(
                eqDisclosure(disclosureEmoji, disclosure),
                eqMember(disclosureEmoji, member),
                eqEmojiType(disclosureEmoji, emojiType)
            )
            .fetchFirst() != null;
    }

    private long countEmojiByType(Disclosure disclosure, EmojiType emojiType) {
        Long count = jpaQueryFactory
            .select(disclosureEmoji.count())
            .from(disclosureEmoji)
            .where(
                eqDisclosure(disclosureEmoji, disclosure),
                eqEmojiType(disclosureEmoji, emojiType)
            )
            .fetchOne();

        return Optional.ofNullable(count).orElse(0L);
    }

    private BooleanExpression eqDisclosure(QDisclosureEmoji disclosureEmoji, Disclosure disclosure) {
        return disclosureEmoji.disclosure.eq(disclosure);
    }

    private BooleanExpression eqMember(QDisclosureEmoji disclosureEmoji, Member member) {
        return disclosureEmoji.member.eq(member);
    }

    private BooleanExpression eqEmojiType(QDisclosureEmoji disclosureEmoji, EmojiType emoji) {
        return disclosureEmoji.emoji.eq(emoji);
    }

    private BooleanExpression isNotDeleted(QDisclosure disclosure) {
        return disclosure.deletedAt.isNull();
    }
}

