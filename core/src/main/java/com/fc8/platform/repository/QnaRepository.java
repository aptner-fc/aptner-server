package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.enums.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaRepository {

    Qna store(Qna qna);

    Page<Qna> getQnaListByApartCode(Long memberId, String apartCode, Pageable pageable, String search, SearchType type, String categoryCode);

    Qna getQnaWithCategoryByIdAndApartCode(Long qnaId, String apartCode);

    boolean isWriter(Qna qna, Member member);

    Qna getByIdAndApartCode(Long qnaId, String apartCode);

    Qna getByIdAndMemberId(Long postId, Long memberId);
}
