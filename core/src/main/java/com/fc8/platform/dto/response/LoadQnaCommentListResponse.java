package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.CommentInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadQnaCommentListResponse {

    private final List<CommentInfo> comments;

    public LoadQnaCommentListResponse(List<CommentInfo> comments) {
        this.comments = comments;
    }
}
