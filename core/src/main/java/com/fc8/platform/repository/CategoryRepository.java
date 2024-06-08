package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.category.Category;

import java.util.List;

public interface CategoryRepository {

    Category getChildCategoryByCode(String code);

    List<Category> getParentCategory();
}
