package com.fc8.server.impl;

import com.fc8.platform.domain.entity.qna.QnaFile;
import com.fc8.platform.repository.QnaFileRepository;
import com.fc8.server.QnaFileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QnaFileRepositoryImpl implements QnaFileRepository {

    private final QnaFileJpaRepository qnaFileJpaRepository;


    @Override
    public QnaFile store(QnaFile qnaFile) {
        return qnaFileJpaRepository.save(qnaFile);
    }
}
