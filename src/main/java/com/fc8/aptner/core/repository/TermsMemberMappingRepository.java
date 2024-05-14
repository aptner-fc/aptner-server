package com.fc8.aptner.core.repository;

import com.fc8.aptner.core.domain.entity.mapping.TermsMemberMapping;

import java.util.List;

public interface TermsMemberMappingRepository {

    List<TermsMemberMapping> storeAll(List<TermsMemberMapping> termsMemberMappings);
}
