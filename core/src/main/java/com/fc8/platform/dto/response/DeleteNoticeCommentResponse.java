package com.fc8.platform.dto.response;

import lombok.Getter;

@Getter
public class DeleteNoticeCommentResponse {

    private final Long NoticeCommentId;

    public DeleteNoticeCommentResponse(Long noticeCommentId) {
        NoticeCommentId = noticeCommentId;
    }
}
