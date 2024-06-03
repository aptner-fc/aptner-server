package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.admin.QAdmin;
import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.notice.QNotice;
import com.fc8.platform.domain.entity.qna.QQna;
import com.fc8.platform.repository.NoticeRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QNotice notice = QNotice.notice;
    QCategory category = QCategory.category;
    QMember member = QMember.member;
    QAdmin admin = QAdmin.admin;

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

    private BooleanExpression eqId(QNotice notice, Long noticeId) {
        return notice.id.eq(noticeId);
    }

    private BooleanExpression eqApartCode(QNotice notice, String apartCode) {
        return notice.apart.code.eq(apartCode);
    }
}
