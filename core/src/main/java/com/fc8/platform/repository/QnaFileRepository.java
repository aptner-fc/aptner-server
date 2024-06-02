package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaFile;

import java.util.List;

public interface QnaFileRepository {

    QnaFile store(QnaFile qnaFile);

    List<QnaFile> getQnaFileListByQna(Qna qna);

}
