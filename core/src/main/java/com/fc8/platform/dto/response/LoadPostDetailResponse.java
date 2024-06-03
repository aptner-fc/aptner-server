package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PostDetailInfo;
import com.fc8.platform.dto.record.PostFileInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadPostDetailResponse {

    PostDetailInfo post;
    List<PostFileInfo> postFileInfoList;

    public LoadPostDetailResponse(PostDetailInfo post, List<PostFileInfo> postFileInfoList) {
        this.post = post;
        this.postFileInfoList = postFileInfoList;
    }
}
