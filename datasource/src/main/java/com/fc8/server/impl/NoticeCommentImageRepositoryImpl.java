package com.fc8.server.impl;

import com.fc8.platform.domain.entity.notice.NoticeCommentImage;
import com.fc8.platform.repository.NoticeCommentImageRepository;
import com.fc8.server.NoticeCommentImageJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeCommentImageRepositoryImpl implements NoticeCommentImageRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final NoticeCommentImageJpaRepository noticeCommentImageJpaRepository;

    @Override
    public NoticeCommentImage store(NoticeCommentImage noticeCommentImage) {
        return noticeCommentImageJpaRepository.save(noticeCommentImage);
    }
}
