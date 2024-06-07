package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.enums.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DisclosureRepository {
    Disclosure getDisclosureWithCategoryByIdAndApartCode(Long disclosureId, String apartCode);

    Page<Disclosure> getDisclosureListByApartCode(Long memberId, String apartCode, Pageable pageable, String search, SearchType type, String categoryCode);
}

