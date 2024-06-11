package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.admin.QAdmin;
import com.fc8.platform.domain.entity.apartment.QApart;
import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.notice.QNotice;
import com.fc8.platform.domain.entity.notice.QNoticeComment;
import com.fc8.platform.domain.entity.notice.QNoticeFile;
import com.fc8.platform.domain.enums.SearchType;
import com.fc8.platform.dto.record.CategoryInfo;
import com.fc8.platform.dto.record.NoticeInfo;
import com.fc8.platform.dto.record.WriterInfo;
import com.fc8.platform.repository.NoticeRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QNotice notice = QNotice.notice;
    QCategory category = QCategory.category;
    QAdmin admin = QAdmin.admin;
    QApart apart = QApart.apart;
    QNoticeComment noticeComment = QNoticeComment.noticeComment;
    QNoticeFile noticeFile = QNoticeFile.noticeFile;

    @Override
    public Notice getNoticeWithCategoryByIdAndApartCode(Long noticeId, String apartCode) {
        Notice activeNotice = jpaQueryFactory
            .selectFrom(notice)
            .innerJoin(category).on(notice.category.id.eq(category.id))
            .where(
                eqId(notice, noticeId),
                eqApartCode(notice, apartCode)
            )
            .fetchOne();

        return Optional.ofNullable(activeNotice)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public Page<NoticeInfo> getNoticeInfoList(Long memberId, String apartCode, Pageable pageable, String search, SearchType type, String categoryCode) {
        List<NoticeInfo> noticeInfoList = jpaQueryFactory
            .select(Projections.constructor(
                NoticeInfo.class,
                notice.id,
                notice.title,
                notice.createdAt,
                notice.updatedAt,
                Projections.constructor(
                    WriterInfo.class,
                    notice.admin.id,
                    notice.admin.name,
                    notice.admin.nickname
                ),
                Projections.constructor(
                    CategoryInfo.class,
                    notice.category.id,
                    notice.category.type,
                    notice.category.code,
                    notice.category.name
                ),
                jpaQueryFactory.select(noticeComment.count())
                    .from(noticeComment)
                    .where(noticeComment.notice.id.eq(notice.id)),
                jpaQueryFactory.select(noticeFile.count())
                    .from(noticeFile)
                    .where(noticeFile.notice.id.eq(notice.id)).gt(0L)
            ))
            .from(notice)
            .innerJoin(category).on(notice.category.id.eq(category.id))
            .innerJoin(admin).on(notice.admin.id.eq(admin.id))
            .where(
                // 1. 삭제된 게시글
                isNotDeleted(notice),
                // 2. 아파트 코드
                eqApartCode(notice, apartCode),
                // 4. 카테고리
                eqCategoryCode(category, categoryCode),
                // 5. 검색어
                containsSearch(notice, admin, search, type)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(notice.createdAt.desc())
            .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(notice.count())
            .from(notice)
            .innerJoin(notice.category, category)
            .where(
                isNotDeleted(notice),
                eqApartCode(notice, apartCode),
                eqCategoryCode(category, categoryCode),
                containsSearch(notice, admin, search, type)
            );

        return PageableExecutionUtils.getPage(noticeInfoList, pageable, countQuery::fetchOne);
    }

    @Override
    public List<Notice> getNoticeListByKeyword(String apartCode, String keyword, int pinnedNoticeCount) {
        return jpaQueryFactory
            .selectFrom(notice)
            .innerJoin(apart).on(notice.apart.eq(apart))
            .where(
                // 아파트 체크
                apart.code.eq(apartCode),

                // 삭제 여부
                isNotDeleted(notice),

                // 검색어
                containsSearch(notice, null, keyword, SearchType.TITLE_AND_CONTENT)
            )
            .limit(5 - pinnedNoticeCount)
            .orderBy(notice.createdAt.desc())
            .fetch();
    }

    @Override
    public Long getNoticeCountByKeyword(String apartCode, String keyword) {
        Long count = jpaQueryFactory
            .select(notice.count())
            .from(notice)
            .innerJoin(apart).on(notice.apart.eq(apart))
            .where(
                // 아파트 체크
                apart.code.eq(apartCode),

                // 삭제 여부
                isNotDeleted(notice),

                // 검색어
                containsSearch(notice, null, keyword, SearchType.TITLE_AND_CONTENT)
            )
            .fetchOne();

        return count != null ? count : 0;
    }

    @Override
    public Notice getByIdAndApartCode(Long noticeId, String apartCode) {
        Notice activeNotice = jpaQueryFactory
            .selectFrom(notice)
            .where(
                eqId(notice, noticeId),
                eqApartCode(notice, apartCode),
                isNotDeleted(notice)
            )
            .fetchOne();

        return Optional.ofNullable(activeNotice)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    private BooleanExpression eqId(QNotice notice, Long noticeId) {
        return notice.id.eq(noticeId);
    }

    private BooleanExpression isNotDeleted(QNotice notice) {
        return notice.deletedAt.isNull();
    }

    private BooleanExpression eqCategoryCode(QCategory category, String categoryCode) {
        if (!StringUtils.hasText(categoryCode)) {
            return null;
        }

        return category.code.eq(categoryCode);
    }

    private BooleanExpression containsSearch(QNotice notice, QAdmin admin, String search, SearchType type) {
        if (!StringUtils.hasText(search)) {
            return null;
        }

        return switch (type) {
            case TITLE -> notice.title.contains(search);
            case CONTENT -> notice.content.contains(search);
            case TITLE_AND_CONTENT -> notice.title.contains(search).or(notice.content.contains(search));
            case WRITER -> admin.name.contains(search).or(admin.nickname.contains(search));
        };
    }

    private BooleanExpression eqApartCode(QNotice notice, String apartCode) {
        return notice.apart.code.eq(apartCode);
    }
}
