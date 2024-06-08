package com.fc8.facade;

import com.fc8.platform.dto.response.LoadParentCategoryListResponse;
import com.fc8.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryFacade {

    private final CategoryService categoryService;


    public LoadParentCategoryListResponse loadUsedParentCategoryList() {
        return new LoadParentCategoryListResponse(categoryService.loadUsedParentCategoryList());
    }
}
