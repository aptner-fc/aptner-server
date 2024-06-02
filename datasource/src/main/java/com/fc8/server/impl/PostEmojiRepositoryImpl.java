package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostEmoji;
import com.fc8.platform.domain.entity.post.QPost;
import com.fc8.platform.domain.entity.post.QPostEmoji;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.EmojiCountInfo;
import com.fc8.platform.dto.record.EmojiReactionInfo;
import com.fc8.platform.repository.PostEmojiRepository;
import com.fc8.server.PostEmojiJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostEmojiRepositoryImpl implements PostEmojiRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PostEmojiJpaRepository postEmojiJpaRepository;

    QPostEmoji postEmoji = QPostEmoji.postEmoji;
    QPost post = QPost.post;
    QMember member = QMember.member;

    @Override
    public PostEmoji store(PostEmoji postEmoji) {
        return postEmojiJpaRepository.save(postEmoji);
    }

    @Override
    public boolean existsByPostAndMemberAndEmoji(Post activePost, Member loginMember, EmojiType emoji) {
        return jpaQueryFactory
                .selectOne()
                .from(postEmoji)
                .innerJoin(post).on(eqPost(postEmoji, activePost))
                .innerJoin(member).on(eqMember(postEmoji, loginMember))
                .where(
                        isNotDeleted(post),
                        eqEmojiType(postEmoji, emoji)
                )
                .fetchFirst() != null;
    }

    @Override
    public PostEmoji getByPostAndMemberAndEmoji(Post activePost, Member loginMember, EmojiType emoji) {
        PostEmoji addedEmoji = jpaQueryFactory
                .selectFrom(postEmoji)
                .innerJoin(post).on(eqPost(postEmoji, activePost))
                .innerJoin(member).on(eqMember(postEmoji, loginMember))
                .where(
                        isNotDeleted(post),
                        eqEmojiType(postEmoji, emoji)
                )
                .fetchOne();

        return Optional.ofNullable(addedEmoji)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_EMOJI));
    }

    @Override
    public void delete(PostEmoji postEmoji) {
        postEmojiJpaRepository.delete(postEmoji);
    }

    @Override
    public EmojiCountInfo getEmojiCountInfoByPostAndMember(Post post) {
        long likeCount = countEmojiByType(post, EmojiType.LIKE);
        long empathyCount = countEmojiByType(post, EmojiType.EMPATHY);
        long funCount = countEmojiByType(post, EmojiType.FUN);
        long amazingCount = countEmojiByType(post, EmojiType.AMAZING);
        long sadCount = countEmojiByType(post, EmojiType.SAD);

        return new EmojiCountInfo(likeCount, empathyCount, funCount, amazingCount, sadCount);
    }

    @Override
    public EmojiReactionInfo getEmojiReactionInfoByPostAndMember(Post post, Member member) {
        boolean reactedLike = reactedEmoji(post, member, EmojiType.LIKE);
        boolean reactedEmpathy = reactedEmoji(post, member, EmojiType.EMPATHY);
        boolean reactedFun = reactedEmoji(post, member, EmojiType.FUN);
        boolean reactedAmazing = reactedEmoji(post, member, EmojiType.AMAZING);
        boolean reactedSad = reactedEmoji(post, member, EmojiType.SAD);

        return new EmojiReactionInfo(reactedLike, reactedEmpathy, reactedFun, reactedAmazing, reactedSad);
    }

    private boolean reactedEmoji(Post post, Member member, EmojiType emojiType) {
        return jpaQueryFactory
                .selectOne()
                .from(postEmoji)
                .where(
                        eqPost(postEmoji, post),
                        eqMember(postEmoji, member),
                        eqEmojiType(postEmoji, emojiType)
                )
                .fetchFirst() != null;
    }

    private BooleanExpression eqMember(QPostEmoji postEmoji, Member member) {
        return postEmoji.member.eq(member);
    }

    private BooleanExpression eqPost(QPostEmoji postEmoji, Post post) {
        return postEmoji.post.eq(post);
    }

    private long countEmojiByType(Post post, EmojiType emojiType) {
        Long count = jpaQueryFactory
                .select(postEmoji.count())
                .from(postEmoji)
                .where(
                        eqPost(postEmoji, post),
                        eqEmojiType(postEmoji, emojiType)
                )
                .fetchOne();

        return Optional.ofNullable(count).orElse(0L);
    }

    private BooleanExpression isNotDeleted(QPost post) {
        return post.deletedAt.isNull();
    }

    private BooleanExpression eqEmojiType(QPostEmoji postEmoji, EmojiType emoji) {
        return postEmoji.emoji.eq(emoji);
    }
}
