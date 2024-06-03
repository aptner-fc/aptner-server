package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.DeletedCountInfo;
import lombok.Getter;

@Getter
public class DeleteMyCommentListResponse {

    private final int deletedCount;

    public DeleteMyCommentListResponse(int deletedCount) {
        this.deletedCount = deletedCount;
    }

    public DeleteMyCommentListResponse(DeletedCountInfo deletedCountInfo) {
        this.deletedCount = deletedCountInfo.deletedCount();
    }
}
