package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.qna.QnaCommentImage;

public interface QnaCommentImageRepository {

    QnaCommentImage store(QnaCommentImage qnaCommentImage);

    QnaCommentImage getImageByQnaCommentId(Long commentId);
}
