package com.fc8.service;

import com.fc8.platform.dto.record.ParentCategoryInfo;

import java.util.List;

public interface CategoryService {
    List<ParentCategoryInfo> loadUsedParentCategoryList();
}
