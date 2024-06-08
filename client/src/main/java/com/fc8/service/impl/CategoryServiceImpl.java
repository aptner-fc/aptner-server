package com.fc8.service.impl;

import com.fc8.platform.dto.record.ChildCategoryInfo;
import com.fc8.platform.dto.record.ParentCategoryInfo;
import com.fc8.platform.repository.CategoryRepository;
import com.fc8.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    public List<ParentCategoryInfo> loadUsedParentCategoryList() {
        var categoryList = categoryRepository.getParentCategory();
        return ParentCategoryInfo.fromEntityList(categoryList);
    }

    @Override
    public List<ChildCategoryInfo> loadUsedChildCategoryList(Long parentCategoryId) {
        var categoryList = categoryRepository.getAllChildCategoryByParentId(parentCategoryId);
        return ChildCategoryInfo.fromEntityList(categoryList);
    }
}
