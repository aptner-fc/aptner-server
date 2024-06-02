package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.LoadMyArticleInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadMyArticleListResponse {

    List<LoadMyArticleInfo> myArticleList;

    public LoadMyArticleListResponse(List<LoadMyArticleInfo> myArticleList) {
        this.myArticleList = myArticleList;
    }
}
