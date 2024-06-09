package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.LoadMyCommentInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadMyCommentListResponse {

    private final List<LoadMyCommentInfo> myCommentList;

    public LoadMyCommentListResponse(List<LoadMyCommentInfo> myCommentList) {
        this.myCommentList = myCommentList;
    }
}
