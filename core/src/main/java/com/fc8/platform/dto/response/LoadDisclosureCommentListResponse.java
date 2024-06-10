package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.CommentInfo;
import com.fc8.platform.dto.record.DisclosureCommentInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadDisclosureCommentListResponse {

    private final List<CommentInfo> comments;

    public LoadDisclosureCommentListResponse(List<CommentInfo> comments) {
        this.comments = comments;
    }
}
