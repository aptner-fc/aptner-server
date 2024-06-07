package com.fc8.server.impl;

import com.fc8.platform.domain.entity.disclosure.QDisclosure;
import com.fc8.platform.domain.entity.disclosure.QDisclosureComment;
import com.fc8.platform.domain.entity.disclosure.QDisclosureCommentImage;
import com.fc8.platform.dto.record.DisclosureCommentInfo;
import com.fc8.platform.dto.record.WriterInfo;
import com.fc8.platform.repository.DisclosureCommentRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DisclosureCommentRepositoryImpl implements DisclosureCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QDisclosureComment disclosureComment = QDisclosureComment.disclosureComment;
    QDisclosure disclosure = QDisclosure.disclosure;
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
}

