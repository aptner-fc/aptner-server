package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PostCommentInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadPostCommentListResponse {

    List<PostCommentInfo> comments;

    public LoadPostCommentListResponse(List<PostCommentInfo> comments) {
        this.comments = comments;
    }
}
