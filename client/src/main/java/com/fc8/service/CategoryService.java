package com.fc8.service;

import com.fc8.platform.dto.record.ChildCategoryInfo;
import com.fc8.platform.dto.record.ParentCategoryInfo;

import java.util.List;

public interface CategoryService {
    List<ParentCategoryInfo> loadUsedParentCategoryList();

    List<ChildCategoryInfo> loadUsedChildCategoryList(Long parentCategoryId);
}
