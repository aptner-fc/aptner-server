package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.DisclosureCommentInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadDisclosureCommentListResponse {

    private final List<DisclosureCommentInfo> comments;

    public LoadDisclosureCommentListResponse(List<DisclosureCommentInfo> comments) {
        this.comments = comments;
    }
}
