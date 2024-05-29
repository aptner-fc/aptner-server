package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.QnaCommentInfo;

import java.util.List;

public class LoadQnaCommentListResponse {

    List<QnaCommentInfo> comments;

    public LoadQnaCommentListResponse(List<QnaCommentInfo> comments) {
        this.comments = comments;
    }
}
