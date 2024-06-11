package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.enums.SearchType;
import com.fc8.platform.dto.record.DisclosureInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DisclosureRepository {
    Disclosure getDisclosureWithCategoryByIdAndApartCode(Long disclosureId, String apartCode);

    List<Disclosure> getDisclosureListByKeyword(String apartCode, String keyword, int pinnedDisclosureCount);

    Long getDisclosureCountByKeyword(String apartCode, String keyword);

    Disclosure getByIdAndApartCode(Long disclosureId, String apartCode);

    Page<DisclosureInfo> getDisclosureInfoList(Long memberId, String apartCode, Pageable pageable, String search, SearchType type, String categoryCode);

    void updateViewCount(Long disclosureId, Long viewCount);
}

