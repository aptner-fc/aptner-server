package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.disclosure.*;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.member.QMemberBlock;
import com.fc8.platform.dto.record.DisclosureCommentInfo;
import com.fc8.platform.dto.record.WriterInfo;
import com.fc8.platform.repository.DisclosureCommentRepository;
import com.fc8.server.DisclosureCommentJpaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class DisclosureCommentRepositoryImpl implements DisclosureCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final DisclosureCommentJpaRepository disclosureCommentJpaRepository;

    QDisclosureComment disclosureComment = QDisclosureComment.disclosureComment;
    QDisclosure disclosure = QDisclosure.disclosure;
    QMember member = QMember.member;
    QMemberBlock memberBlock = QMemberBlock.memberBlock;
    QDisclosureCommentImage disclosureCommentImage = QDisclosureCommentImage.disclosureCommentImage;

    @Override
    public Page<DisclosureCommentInfo> getCommentListByDisclosure(Long disclosureId, Pageable pageable) {
        List<DisclosureCommentInfo> commentList = jpaQueryFactory
            .select(Projections.constructor(
                DisclosureCommentInfo.class,
                disclosureComment.id,
                disclosureComment.parent.id,
                disclosureComment.content,
                disclosureComment.createdAt,
                disclosureComment.updatedAt,
                disclosureCommentImage.imagePath.as("imageUrl"),
                Projections.constructor(
                    WriterInfo.class,
                    disclosureComment.member.id,
                    disclosureComment.member.name,
                    disclosureComment.member.nickname
                )
            ))
            .from(disclosureComment)
            .innerJoin(disclosure).on(disclosureComment.disclosure.id.eq(disclosure.id))
            .leftJoin(disclosureCommentImage).on(disclosureCommentImage.disclosureComment.id.eq(disclosureComment.id))
            .where(
                disclosureComment.disclosure.id.eq(disclosureId),
                disclosureComment.deletedAt.isNull() // 삭제되지 않은 댓글만 조회
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(disclosureComment.count())
            .from(disclosureComment)
            .innerJoin(disclosureComment.disclosure, this.disclosure)
            .where(
                disclosureComment.disclosure.eq(disclosure),
                disclosureComment.deletedAt.isNull() // 삭제되지 않은 댓글만 조회
            );

        return PageableExecutionUtils.getPage(commentList, pageable, countQuery::fetchOne);
    }

    @Override
    public DisclosureComment getByIdAndDisclosure(Long commentId, Disclosure disclosure) {
        DisclosureComment activeComment = jpaQueryFactory
            .selectFrom(disclosureComment)
            .where(
                eqId(commentId),
                eqDisclosure(disclosure)
            )
            .fetchOne();

        return Optional.ofNullable(activeComment)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));
    }

    @Override
    public DisclosureComment store(DisclosureComment disclosureComment) {
        return disclosureCommentJpaRepository.save(disclosureComment);
    }

    @Override
    public DisclosureComment getByIdAndDisclosureIdAndMemberId(Long commentId, Long disclosureId, Long memberId) {
        DisclosureComment writtenComment = jpaQueryFactory
            .selectFrom(disclosureComment)
            .innerJoin(disclosure).on(disclosureComment.disclosure.id.eq(disclosure.id))
            .innerJoin(member).on(disclosureComment.member.id.eq(member.id))
            .where(
                eqId(commentId),
                eqDisclosureCommentDisclosure(disclosureComment.disclosure, disclosureId),
                eqDisclosureCommentWriter(disclosureComment.member, memberId),
                isNotDeleted()
            )
            .fetchOne();

        return Optional.ofNullable(writtenComment)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST_COMMENT));
    }

    @Override
    public boolean isWriter(DisclosureComment comment, Member member) {
        return jpaQueryFactory
            .selectOne()
            .from(disclosureComment)
            .innerJoin(disclosureComment.member, this.member)
            .where(
                disclosureComment.eq(comment),
                this.member.eq(member)
            )
            .fetchFirst() != null;

    }

    @Override
    public Page<DisclosureComment> getAllByDisclosureIdAndMemberId(Long disclosureId, Long memberId, Pageable pageable) {
        QDisclosureComment parent = new QDisclosureComment("parent");
        List<DisclosureComment> commentList = jpaQueryFactory
            .selectFrom(disclosureComment)
            .innerJoin(disclosure).on(disclosureComment.disclosure.eq(disclosure))
            .leftJoin(parent).on(disclosureComment.parent.eq(parent))
            .where(
                disclosure.id.eq(disclosureId),
                disclosure.deletedAt.isNull()
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(disclosureComment.createdAt.asc())
            .fetch();

        List<Long> blockedOrBlockingMemberIds = getBlockedOrBlockingMemberIds(memberId);

        // 차단된 댓글 여부 설정
        commentList.forEach(comment -> {
            if (blockedOrBlockingMemberIds.contains(comment.getMember().getId())) {
                comment.changeBlockStatus(true);
            }
        });

        JPAQuery<Long> count = jpaQueryFactory
            .select(disclosureComment.count())
            .from(disclosureComment)
            .innerJoin(disclosure).on(disclosureComment.disclosure.eq(disclosure))
            .where(
                disclosure.id.eq(disclosureId),
                disclosure.deletedAt.isNull()
            );

        return PageableExecutionUtils.getPage(commentList, pageable, count::fetchOne);
    }

    private List<Long> getBlockedOrBlockingMemberIds(Long memberId) {
        List<Long> blockedMemberIds = getBlockedMemberIds(memberId);
        List<Long> blockingMemberIds = getBlockingMemberIds(memberId);

        return Stream.concat(blockedMemberIds.stream(), blockingMemberIds.stream())
            .distinct()
            .toList();
    }

    private List<Long> getBlockedMemberIds(Long memberId) {
        return jpaQueryFactory
            .select(memberBlock.blocked.id)
            .from(memberBlock)
            .where(memberBlock.member.id.eq(memberId))
            .fetch();
    }

    private List<Long> getBlockingMemberIds(Long memberId) {
        return jpaQueryFactory
            .select(memberBlock.member.id)
            .from(memberBlock)
            .where(memberBlock.blocked.id.eq(memberId))
            .fetch();
    }

    private BooleanExpression isNotDeleted() {
        return disclosureComment.deletedAt.isNull();
    }

    private BooleanExpression eqDisclosureCommentWriter(QMember member, Long memberId) {
        return member.id.eq(memberId);
    }

    private BooleanExpression eqDisclosureCommentDisclosure(QDisclosure disclosure, Long disclosureId) {
        return disclosure.id.eq(disclosureId);
    }

    private BooleanExpression eqDisclosure(Disclosure disclosure) {
        return disclosureComment.disclosure.eq(disclosure);
    }

    private BooleanExpression eqId(Long commentId) {
        return disclosureComment.id.eq(commentId);
    }
}

