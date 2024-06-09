package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PinnedPostDetailInfo;
import com.fc8.platform.dto.record.PostFileInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadPinnedPostDetailResponse {

    private final PinnedPostDetailInfo pinnedPost;
    private final List<PostFileInfo> pinnedPostFileInfoList;

    public LoadPinnedPostDetailResponse(PinnedPostDetailInfo pinnedPost, List<PostFileInfo> pinnedPostFileInfoList) {
        this.pinnedPost = pinnedPost;
        this.pinnedPostFileInfoList = pinnedPostFileInfoList;
    }
}
