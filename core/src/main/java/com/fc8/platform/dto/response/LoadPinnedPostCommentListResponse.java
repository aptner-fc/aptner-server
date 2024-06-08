package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.CommentInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadPinnedPostCommentListResponse {

    private final List<CommentInfo> comments;

    public LoadPinnedPostCommentListResponse(List<CommentInfo> comments) {
        this.comments = comments;
    }
}
