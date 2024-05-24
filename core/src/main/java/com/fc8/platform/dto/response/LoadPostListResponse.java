package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PostInfo;
import com.fc8.platform.dto.record.PinnedPost;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadPostListResponse {

    List<PostInfo> posts;

    List<PinnedPost> pinnedPost;

    public LoadPostListResponse(List<PostInfo> posts, List<PinnedPost> pinnedPost) {
        this.posts = posts;
        this.pinnedPost = pinnedPost;
    }
}
