package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PostDetailInfo;
import lombok.Getter;

@Getter
public class LoadPostDetailResponse {

    PostDetailInfo post;

    public LoadPostDetailResponse(PostDetailInfo post) {
        this.post = post;
    }
}
