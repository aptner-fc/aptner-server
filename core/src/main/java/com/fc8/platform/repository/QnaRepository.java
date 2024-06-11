package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.enums.SearchType;
import com.fc8.platform.dto.record.QnaInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QnaRepository {

    Qna store(Qna qna);

    Qna getQnaWithCategoryByIdAndApartCode(Long memberId, Long qnaId, String apartCode);

    boolean isWriter(Qna qna, Member member);

    Qna getByIdAndApartCode(Long qnaId, String apartCode);

    Qna getByIdAndMemberId(Long postId, Long memberId);

    List<Qna> getAllByIdsAndMember(List<Long> postIds, Member member);

    Qna getById(Long qnaId);

    List<Qna> getQnaListByKeyword(Long memberId, String apartCode, String keyword, int pinnedQnaCount);

    Long getQnaCountByKeyword(Long memberId, String apartCode, String keyword);

    Page<QnaInfo> getQnaInfoList(Long memberId, String apartCode, Pageable pageable, String search, SearchType type, String categoryCode);

    void updateViewCount(Long qnaId, Long viewCount);
}
