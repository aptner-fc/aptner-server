package com.fc8.server.impl;

import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.server.QnaJpaRepository;
import com.fc8.server.QnaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QnaRepositoryImpl implements QnaRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QnaJpaRepository qnaJpaRepository;

    @Override
    public Qna store(Qna qna) {
        return qnaJpaRepository.save(qna);
    }

}
