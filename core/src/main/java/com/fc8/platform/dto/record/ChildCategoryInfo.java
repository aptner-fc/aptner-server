package com.fc8.platform.dto.record;

import com.fc8.platform.domain.entity.category.Category;
import com.fc8.platform.domain.enums.CategoryType;
import lombok.Builder;

import java.util.List;

@Builder
public record ChildCategoryInfo(
    Long id,
    CategoryType type,
    String code,
    String name,
    Long parentId
) {
    public static ChildCategoryInfo fromEntity(Category category) {
        return ChildCategoryInfo.builder()
            .id(category.getId())
            .type(category.getType())
            .code(category.getCode())
            .name(category.getName())
            .parentId(category.getParent().getId())
            .build();
    }

    public static List<ChildCategoryInfo> fromEntityList(List<Category> categoryList) {
        return categoryList.stream()
            .map(ChildCategoryInfo::fromEntity)
            .toList();
    }
}
