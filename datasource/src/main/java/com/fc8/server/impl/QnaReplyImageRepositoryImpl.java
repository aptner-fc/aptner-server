package com.fc8.server.impl;

import com.fc8.platform.domain.entity.qna.QnaReplyImage;
import com.fc8.platform.repository.QnaReplyImageRepository;
import com.fc8.server.QnaReplyImageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QnaReplyImageRepositoryImpl implements QnaReplyImageRepository {


    private final QnaReplyImageJpaRepository qnaReplyImageJpaRepository;

    @Override
    public QnaReplyImage store(QnaReplyImage qnaReplyImage) {
        return qnaReplyImageJpaRepository.save(qnaReplyImage);
    }
}
