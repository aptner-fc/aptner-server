package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class WriteNoticeCommentResponse {

    private final Long NoticeCommentId;

    public WriteNoticeCommentResponse(Long noticeCommentId) {
        NoticeCommentId = noticeCommentId;
    }
}
