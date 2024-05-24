package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.category.Category;

public interface CategoryRepository {

    Category getChildCategoryByCode(String code);
}
