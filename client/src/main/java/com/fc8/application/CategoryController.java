package com.fc8.application;

import com.fc8.facade.CategoryFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.dto.response.LoadChildCategoryListResponse;
import com.fc8.platform.dto.response.LoadParentCategoryListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "카테고리 관련 API")
@RestController
@RequestMapping(value = {"/v1/api/categories"})
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryFacade categoryFacade;

    @Operation(summary = "부모 카테고리 조회 API")
    @GetMapping("/parents")
    public ResponseEntity<CommonResponse<LoadParentCategoryListResponse>> loadUsedParentCategoryList() {
        return CommonResponse.success(SuccessCode.SUCCESS, categoryFacade.loadUsedParentCategoryList());
    }

    @Operation(summary = "자식 카테고리 조회 API")
    @GetMapping("/children/{parentCategoryId}")
    public ResponseEntity<CommonResponse<LoadChildCategoryListResponse>> loadUsedChildCategoryList(
        @PathVariable Long parentCategoryId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, categoryFacade.loadUsedChildCategoryList(parentCategoryId));
    }

}
