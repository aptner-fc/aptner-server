package com.fc8.server.impl;

import com.fc8.platform.domain.entity.qna.QnaAnswer;
import com.fc8.platform.repository.QnaAnswerRepository;
import com.fc8.server.QnaAnswerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QnaAnswerRepositoryImpl implements QnaAnswerRepository {

    private final QnaAnswerJpaRepository qnaAnswerJpaRepository;

    @Override
    public QnaAnswer store(QnaAnswer qnaAnswer) {
        return qnaAnswerJpaRepository.save(qnaAnswer);
    }
}
