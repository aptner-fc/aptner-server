package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PostCommentInfo;

import java.util.List;

public class LoadPostCommentListResponse {

    List<PostCommentInfo> comments;

    public LoadPostCommentListResponse(List<PostCommentInfo> comments) {
        this.comments = comments;
    }
}
