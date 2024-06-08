package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.ParentCategoryInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadParentCategoryListResponse {

    private final List<ParentCategoryInfo> parentCategoryInfoList;

    public LoadParentCategoryListResponse(List<ParentCategoryInfo> parentCategoryInfoList) {
        this.parentCategoryInfoList = parentCategoryInfoList;
    }
}
