package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PinnedPostSummary;
import com.fc8.platform.dto.record.PostSummary;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadPostListResponse {

    List<PostSummary> posts;

    List<PinnedPostSummary> pinnedPosts;

    public LoadPostListResponse(List<PostSummary> posts, List<PinnedPostSummary> pinnedPosts) {
        this.posts = posts;
        this.pinnedPosts = pinnedPosts;
    }
}
