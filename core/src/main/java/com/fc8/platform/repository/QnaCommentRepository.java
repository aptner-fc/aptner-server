package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QnaCommentRepository {

    QnaComment getByIdAndQna(Long id, Qna qna);

    QnaComment store(QnaComment qnaComment);

    boolean isWriter(QnaComment comment, Member member);

    Page<QnaComment> getCommentListByQna(Long memberId, Qna qna, Pageable pageable);

    QnaComment getByIdAndQnaIdAndMemberId(Long id, Long qnaId, Long memberId);

    List<QnaComment> getAllByIdsAndMember(List<Long> qnaCommentIds, Member member);
}
