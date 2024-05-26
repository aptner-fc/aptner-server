package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.qna.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaRepository {

    Qna store(Qna qna);

    Page<Qna> getQnaListByApartCode(Long memberId, String apartCode, Pageable pageable, String search);

    Qna getQnaWithCategoryByIdAndApartCode(Long qnaId, String apartCode);

    void delete(Qna qna);

    Qna getByIdAndApartCode(Long qnaId, String apartCode);
}
