package com.fc8.platform.repository;

import com.fc8.platform.dto.record.DisclosureCommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DisclosureCommentRepository {
    Page<DisclosureCommentInfo> getCommentListByDisclosure(Long disclosureId, Pageable pageable);
}

