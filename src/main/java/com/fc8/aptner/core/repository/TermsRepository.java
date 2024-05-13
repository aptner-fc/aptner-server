package com.fc8.aptner.core.repository;

import com.fc8.aptner.core.domain.entity.terms.Terms;

import java.util.List;

public interface TermsRepository {

    Terms getById(Long id);

    List<Terms> getAllByIds(List<Long> ids);

    List<Terms> getAllByIsUsed();
}
