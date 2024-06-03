package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaComment;
import com.fc8.platform.dto.record.QnaCommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaCommentRepository {

    QnaComment getByIdAndQna(Long id, Qna qna);

    QnaComment store(QnaComment qnaComment);

    boolean isWriter(QnaComment comment, Member member);

    Page<QnaCommentInfo> getCommentListByQna(Long qnaId, Pageable pageable);

    QnaComment getByIdAndQnaIdAndMemberId(Long id, Long qnaId, Long memberId);
}
