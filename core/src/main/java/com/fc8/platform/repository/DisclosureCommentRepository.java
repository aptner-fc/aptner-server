package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.disclosure.DisclosureComment;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.dto.record.DisclosureCommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DisclosureCommentRepository {
    Page<DisclosureCommentInfo> getCommentListByDisclosure(Long disclosureId, Pageable pageable);

    DisclosureComment getByIdAndDisclosure(Long commentId, Disclosure disclosure);

    DisclosureComment store(DisclosureComment disclosureComment);

    DisclosureComment getByIdAndDisclosureIdAndMemberId(Long commentId, Long disclosureId, Long memberId);

    boolean isWriter(DisclosureComment comment, Member member);

    Page<DisclosureComment> getAllByDisclosureIdAndMemberId(Long disclosureId, Long memberId, Pageable pageable);
}

