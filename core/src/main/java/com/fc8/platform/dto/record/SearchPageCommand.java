package com.fc8.platform.dto.record;

import com.fc8.platform.domain.enums.SearchType;
import lombok.Builder;

@Builder
public record SearchPageCommand(int page,
                                int size,
                                String search,
                                SearchType type) {

}
