package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.ChildCategoryInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadChildCategoryListResponse {

    private final List<ChildCategoryInfo> childCategoryInfoList;

    public LoadChildCategoryListResponse(List<ChildCategoryInfo> childCategoryInfoList) {
        this.childCategoryInfoList = childCategoryInfoList;
    }
}
