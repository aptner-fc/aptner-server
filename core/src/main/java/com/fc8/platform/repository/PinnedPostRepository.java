package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.pinned.PinnedPost;

public interface PinnedPostRepository {

    PinnedPost getPinnedPostByIdAndApartCode(Long memberId, Long pinnedPostId, String apartCode);

    PinnedPost getByIdAndApartCode(Long pinnedPostId, String apartCode);

}
