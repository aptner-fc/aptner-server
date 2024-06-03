package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.DeletedCountInfo;
import lombok.Getter;

@Getter
public class DeleteMyArticleListResponse {

    private final int deletedCount;

    public DeleteMyArticleListResponse(int deletedCount) {
        this.deletedCount = deletedCount;
    }

    public DeleteMyArticleListResponse(DeletedCountInfo deletedCountInfo) {
        this.deletedCount = deletedCountInfo.deletedCount();
    }
}
