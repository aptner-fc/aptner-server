package com.fc8.platform.repository;


import com.fc8.platform.domain.entity.mapping.ApartMemberMapping;

import java.util.List;

public interface ApartMemberMappingRepository {

    ApartMemberMapping store(ApartMemberMapping apartMemberMapping);
    List<ApartMemberMapping> storeAll(List<ApartMemberMapping> apartMemberMappings);
}
