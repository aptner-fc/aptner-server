package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.CommentInfo;
import com.fc8.platform.dto.record.PostCommentInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadPostCommentListResponse {

    private final List<CommentInfo> comments;

    public LoadPostCommentListResponse(List<CommentInfo> comments) {
        this.comments = comments;
    }
}
