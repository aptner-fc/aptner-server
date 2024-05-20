package com.fc8.platform.repository;


import com.fc8.platform.domain.entity.mapping.TermsMemberMapping;

import java.util.List;

public interface TermsMemberMappingRepository {

    List<TermsMemberMapping> storeAll(List<TermsMemberMapping> termsMemberMappings);
}
