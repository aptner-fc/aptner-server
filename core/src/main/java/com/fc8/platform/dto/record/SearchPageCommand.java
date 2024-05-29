package com.fc8.platform.dto.record;

import lombok.Builder;

@Builder
public record SearchPageCommand(int page,
                                int size,
                                String search) {

}
