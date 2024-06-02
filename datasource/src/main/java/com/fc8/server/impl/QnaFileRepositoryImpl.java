package com.fc8.server.impl;

import com.fc8.platform.domain.entity.qna.QQnaFile;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaFile;
import com.fc8.platform.repository.QnaFileRepository;
import com.fc8.server.QnaFileJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QnaFileRepositoryImpl implements QnaFileRepository {

    private final QnaFileJpaRepository qnaFileJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    QQnaFile qnaFile = QQnaFile.qnaFile;


    @Override
    public QnaFile store(QnaFile qnaFile) {
        return qnaFileJpaRepository.save(qnaFile);
    }

    @Override
    public List<QnaFile> getQnaFileListByQna(Qna qna) {
        return jpaQueryFactory
            .selectFrom(qnaFile)
            .where(qnaFile.qna.eq(qna))
            .fetch();
    }
}
