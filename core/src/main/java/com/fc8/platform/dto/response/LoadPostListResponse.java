package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PinnedPostInfo;
import com.fc8.platform.dto.record.PostSummary;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadPostListResponse {

    private final List<PostSummary> posts;

    private final List<PinnedPostInfo> pinnedPosts;

    public LoadPostListResponse(List<PostSummary> posts, List<PinnedPostInfo> pinnedPosts) {
        this.posts = posts;
        this.pinnedPosts = pinnedPosts;
    }
}
