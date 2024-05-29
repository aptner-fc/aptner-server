package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.enums.CategoryType;

public record CategoryInfo(Long id,
                           CategoryType type,
                           String code,
                           String name) {

    public static CategoryInfo fromEntity(Category category) {
        return new CategoryInfo(category.getId(), category.getType(), category.getCode(), category.getName());
    }
}
