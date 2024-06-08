package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.pinned.PinnedPostEmoji;
import com.fc8.platform.domain.entity.pinned.QPinnedPost;
import com.fc8.platform.domain.entity.pinned.QPinnedPostEmoji;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;
import com.fc8.platform.repository.PinnedPostEmojiRepository;
import com.fc8.server.PinnedPostEmojiJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PinnedPostEmojiRepositoryImpl implements PinnedPostEmojiRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PinnedPostEmojiJpaRepository pinnedPostEmojiJpaRepository;

    QMember member = QMember.member;
    QPinnedPost pinnedPost = QPinnedPost.pinnedPost;
    QPinnedPostEmoji pinnedPostEmoji = QPinnedPostEmoji.pinnedPostEmoji;

    @Override
    public PinnedPostEmoji store(PinnedPostEmoji pinnedPostEmoji) {
        return pinnedPostEmojiJpaRepository.save(pinnedPostEmoji);
    }

    @Override
    public EmojiCountInfo getEmojiCountInfoByPost(PinnedPost pinnedPost) {
        long likeCount = countEmojiByType(pinnedPost, EmojiType.LIKE);
        long empathyCount = countEmojiByType(pinnedPost, EmojiType.EMPATHY);
        long funCount = countEmojiByType(pinnedPost, EmojiType.FUN);
        long amazingCount = countEmojiByType(pinnedPost, EmojiType.AMAZING);
        long sadCount = countEmojiByType(pinnedPost, EmojiType.SAD);

        return new EmojiCountInfo(likeCount, empathyCount, funCount, amazingCount, sadCount);
    }

    @Override
    public EmojiReactionInfo getEmojiReactionInfoByPostAndMember(PinnedPost pinnedPost, Member member) {
        boolean reactedLike = reactedEmoji(pinnedPost, member, EmojiType.LIKE);
        boolean reactedEmpathy = reactedEmoji(pinnedPost, member, EmojiType.EMPATHY);
        boolean reactedFun = reactedEmoji(pinnedPost, member, EmojiType.FUN);
        boolean reactedAmazing = reactedEmoji(pinnedPost, member, EmojiType.AMAZING);
        boolean reactedSad = reactedEmoji(pinnedPost, member, EmojiType.SAD);

        return new EmojiReactionInfo(reactedLike, reactedEmpathy, reactedFun, reactedAmazing, reactedSad);
    }

    @Override
    public boolean existsByPinnedPostAndMemberAndEmoji(PinnedPost activePinnedPost, Member loginMember, EmojiType emoji) {
        return jpaQueryFactory
                .selectOne()
                .from(pinnedPostEmoji)
                .innerJoin(pinnedPost).on(eqPinnedPost(pinnedPostEmoji, activePinnedPost))
                .innerJoin(member).on(eqMember(pinnedPostEmoji, loginMember))
                .where(
                        isNotDeleted(pinnedPost),
                        eqEmojiType(pinnedPostEmoji, emoji)
                )
                .fetchFirst() != null;
    }

    @Override
    public PinnedPostEmoji getByPinnedPostAndMemberAndEmoji(PinnedPost activePinnedPost, Member loginMember, EmojiType emoji) {
        PinnedPostEmoji addedEmoji = jpaQueryFactory
                .selectFrom(pinnedPostEmoji)
                .innerJoin(pinnedPost).on(eqPinnedPost(pinnedPostEmoji, activePinnedPost))
                .innerJoin(member).on(eqMember(pinnedPostEmoji, loginMember))
                .where(
                        isNotDeleted(pinnedPost),
                        eqEmojiType(pinnedPostEmoji, emoji)
                )
                .fetchOne();

        return Optional.ofNullable(addedEmoji)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_EMOJI));
    }

    @Override
    public void delete(PinnedPostEmoji pinnedPostEmoji) {
        pinnedPostEmojiJpaRepository.delete(pinnedPostEmoji);
    }

    private long countEmojiByType(PinnedPost pinnedPost, EmojiType emojiType) {
        Long count = jpaQueryFactory
                .select(pinnedPostEmoji.count())
                .from(pinnedPostEmoji)
                .where(
                        eqPinnedPost(pinnedPostEmoji, pinnedPost),
                        eqEmojiType(pinnedPostEmoji, emojiType)
                )
                .fetchOne();

        return Optional.ofNullable(count).orElse(0L);
    }

    private boolean reactedEmoji(PinnedPost pinnedPost, Member member, EmojiType emojiType) {
        return jpaQueryFactory
                .selectOne()
                .from(pinnedPostEmoji)
                .where(
                        eqPinnedPost(pinnedPostEmoji, pinnedPost),
                        eqMember(pinnedPostEmoji, member),
                        eqEmojiType(pinnedPostEmoji, emojiType)
                )
                .fetchFirst() != null;
    }

    private BooleanExpression eqPinnedPost(QPinnedPostEmoji qPinnedPostEmoji, PinnedPost pinnedPost) {
        return qPinnedPostEmoji.pinnedPost.eq(pinnedPost);
    }

    private BooleanExpression eqEmojiType(QPinnedPostEmoji qPinnedPostEmoji, EmojiType emoji) {
        return qPinnedPostEmoji.emoji.eq(emoji);
    }

    private BooleanExpression eqMember(QPinnedPostEmoji pinnedPostEmoji, Member member) {
        return pinnedPostEmoji.member.eq(member);
    }

    private BooleanExpression isNotDeleted(QPinnedPost pinnedPost) {
        return pinnedPost.deletedAt.isNull();
    }
}
