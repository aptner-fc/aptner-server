package com.fc8.server.impl;

import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.notice.NoticeFile;
import com.fc8.platform.domain.entity.notice.QNoticeFile;
import com.fc8.platform.repository.NoticeFileRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeFileRepositoryImpl implements NoticeFileRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QNoticeFile noticeFile = QNoticeFile.noticeFile;


    @Override
    public List<NoticeFile> getNoticeFileListByNotice(Notice notice) {
        return jpaQueryFactory
            .selectFrom(noticeFile)
            .where(noticeFile.notice.eq(notice))
            .fetch();
    }
}
