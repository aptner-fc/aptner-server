package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.category.Category;
import lombok.Builder;

import java.util.List;

@Builder
public record ParentCategoryInfo(
    Long id,
    String code,
    String name
) {
    public static ParentCategoryInfo fromEntity(Category category) {
        return ParentCategoryInfo.builder()
            .id(category.getId())
            .code(category.getCode())
            .name(category.getName())
            .build();
    }

    public static List<ParentCategoryInfo> fromEntityList(List<Category> categoryList) {
        return categoryList.stream()
            .map(ParentCategoryInfo::fromEntity)
            .toList();
    }
}
