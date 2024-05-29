package com.fc8.platform.dto.record;

import com.fc8.platform.domain.enums.SortType;
import lombok.Builder;

@Builder
public record CustomPageCommand(int page,
                                int size,
                                SortType sort) {

}
