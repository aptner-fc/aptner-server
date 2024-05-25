package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaComment;

public interface QnaCommentRepository {

    QnaComment getByIdAndQna(Long id, Qna qna);

    QnaComment store(QnaComment qnaComment);

}
