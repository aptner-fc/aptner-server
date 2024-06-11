package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.admin.QAdmin;
import com.fc8.platform.domain.entity.apartment.QApart;
import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.disclosure.QDisclosure;
import com.fc8.platform.domain.entity.disclosure.QDisclosureComment;
import com.fc8.platform.domain.entity.disclosure.QDisclosureFile;
import com.fc8.platform.domain.enums.SearchType;
import com.fc8.platform.dto.record.CategoryInfo;
import com.fc8.platform.dto.record.DisclosureInfo;
import com.fc8.platform.dto.record.WriterInfo;
import com.fc8.platform.repository.DisclosureRepository;
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
public class DisclosureRepositoryImpl implements DisclosureRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QDisclosure disclosure = QDisclosure.disclosure;
    QCategory category = QCategory.category;
    QApart apart = QApart.apart;
    QAdmin admin = QAdmin.admin;
    QDisclosureComment disclosureComment = QDisclosureComment.disclosureComment;
    QDisclosureFile disclosureFile = QDisclosureFile.disclosureFile;

    @Override
    public Disclosure getDisclosureWithCategoryByIdAndApartCode(Long disclosureId, String apartCode) {
        Disclosure activeDisclosure = jpaQueryFactory
            .selectFrom(disclosure)
            .innerJoin(category).on(disclosure.category.id.eq(category.id))
            .where(
                eqId(disclosure, disclosureId),
                eqApartCode(disclosure, apartCode)
            )
            .fetchOne();

        return Optional.ofNullable(activeDisclosure)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public Page<DisclosureInfo> getDisclosureInfoList(Long memberId, String apartCode, Pageable pageable, String search, SearchType type, String categoryCode) {
        List<DisclosureInfo> disclosureInfoList = jpaQueryFactory
                .select(Projections.constructor(
                        DisclosureInfo.class,
                        disclosure.id,
                        disclosure.title,
                        disclosure.createdAt,
                        disclosure.updatedAt,
                        Projections.constructor(
                                WriterInfo.class,
                                disclosure.admin.id,
                                disclosure.admin.name,
                                disclosure.admin.nickname
                        ),
                        Projections.constructor(
                                CategoryInfo.class,
                                disclosure.category.id,
                                disclosure.category.type,
                                disclosure.category.code,
                                disclosure.category.name
                        ),
                        jpaQueryFactory.select(disclosureComment.count())
                                .from(disclosureComment)
                                .where(disclosureComment.disclosure.id.eq(disclosure.id)),
                        disclosure.viewCount,
                        jpaQueryFactory.select(disclosureFile.count())
                                .from(disclosureFile)
                                .where(disclosureFile.disclosure.id.eq(disclosure.id)).gt(0L)
                ))
                .from(disclosure)
                .innerJoin(category).on(disclosure.category.id.eq(category.id))
                .innerJoin(admin).on(disclosure.admin.id.eq(admin.id))
                .where(
                        // 1. 삭제된 게시글
                        isNotDeleted(disclosure),
                        // 2. 아파트 코드
                        eqApartCode(disclosure, apartCode),
                        // 4. 카테고리
                        eqCategoryCode(category, categoryCode),
                        // 5. 검색어
                        containsSearch(disclosure, admin, search, type)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(disclosure.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(disclosure.count())
            .from(disclosure)
            .innerJoin(disclosure.category, category)
            .where(
                isNotDeleted(disclosure),
                eqApartCode(disclosure, apartCode),
                eqCategoryCode(category, categoryCode),
                containsSearch(disclosure, admin, search, type)
            );

        return PageableExecutionUtils.getPage(disclosureInfoList, pageable, countQuery::fetchOne);
    }

    @Override
    public void updateViewCount(Long disclosureId, Long viewCount) {
        jpaQueryFactory
                .update(disclosure)
                .set(disclosure.viewCount, viewCount)
                .where(disclosure.id.eq(disclosureId))
                .execute();
    }

    @Override
    public List<Disclosure> getDisclosureListByKeyword(String apartCode, String keyword, int pinnedDisclosureCount) {
        return jpaQueryFactory
            .selectFrom(disclosure)
            .innerJoin(apart).on(disclosure.apart.eq(apart))
            .where(
                // 아파트 체크
                apart.code.eq(apartCode),

                // 삭제 여부
                isNotDeleted(disclosure),

                // 검색어
                containsSearch(disclosure, null, keyword, SearchType.TITLE_AND_CONTENT)
            )
            .limit(5 - pinnedDisclosureCount)
            .orderBy(disclosure.createdAt.desc())
            .fetch();
    }

    @Override
    public Long getDisclosureCountByKeyword(String apartCode, String keyword) {
        Long count = jpaQueryFactory
            .select(disclosure.count())
            .from(disclosure)
            .innerJoin(apart).on(disclosure.apart.eq(apart))
            .where(
                // 아파트 체크
                apart.code.eq(apartCode),

                // 삭제 여부
                isNotDeleted(disclosure),

                // 검색어
                containsSearch(disclosure, null, keyword, SearchType.TITLE_AND_CONTENT)
            )
            .fetchOne();

        return count != null ? count : 0;
    }

    @Override
    public Disclosure getByIdAndApartCode(Long disclosureId, String apartCode) {
        Disclosure activeDisclosure = jpaQueryFactory
            .selectFrom(disclosure)
            .where(
                eqId(disclosure, disclosureId),
                eqApartCode(disclosure, apartCode),
                isNotDeleted(disclosure)
            )
            .fetchOne();

        return Optional.ofNullable(activeDisclosure)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_POST));
    }

    private BooleanExpression eqId(QDisclosure disclosure, Long disclosureId) {
        return disclosure.id.eq(disclosureId);
    }

    private BooleanExpression isNotDeleted(QDisclosure disclosure) {
        return disclosure.deletedAt.isNull();
    }

    private BooleanExpression eqCategoryCode(QCategory category, String categoryCode) {
        if (!StringUtils.hasText(categoryCode)) {
            return null;
        }

        return category.code.eq(categoryCode);
    }

    private BooleanExpression containsSearch(QDisclosure disclosure, QAdmin admin, String search, SearchType type) {
        if (!StringUtils.hasText(search)) {
            return null;
        }

        return switch (type) {
            case TITLE -> disclosure.title.contains(search);
            case CONTENT -> disclosure.content.contains(search);
            case TITLE_AND_CONTENT -> disclosure.title.contains(search).or(disclosure.content.contains(search));
            case WRITER -> admin.name.contains(search).or(admin.nickname.contains(search));
        };
    }

    private BooleanExpression eqApartCode(QDisclosure disclosure, String apartCode) {
        return disclosure.apart.code.eq(apartCode);
    }
}

