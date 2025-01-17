package com.fc8.platform.repository;


import com.fc8.platform.domain.entity.terms.Terms;

import java.util.List;

public interface TermsRepository {

    Terms getById(Long id);

    List<Terms> getAllByIds(List<Long> ids);

    List<Terms> getAllByIsUsed();

    Terms store(Terms terms);

}
