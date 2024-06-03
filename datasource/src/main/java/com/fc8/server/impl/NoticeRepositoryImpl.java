package com.fc8.server.impl;

import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.repository.NoticeRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

    private final JPAQueryFactory jpaQueryFactory;



    @Override
    public Notice getNoticeWithCategoryByIdAndApartCode(Long noticeId, String apartCode) {
//        Notice activeNotice = jpaQueryFactory
//            .selectFrom(notice)
        return null;
    }
}
