package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PinnedPost;
import com.fc8.platform.dto.record.PostSummary;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadPostListResponse {

    List<PostSummary> posts;

    List<PinnedPost> pinnedPost;

    public LoadPostListResponse(List<PostSummary> posts, List<PinnedPost> pinnedPost) {
        this.posts = posts;
        this.pinnedPost = pinnedPost;
    }
}
