package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.NoticeCommentInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadNoticeCommentListResponse {

    List<NoticeCommentInfo> comments;

    public LoadNoticeCommentListResponse(List<NoticeCommentInfo> comments) {
        this.comments = comments;
    }
}
