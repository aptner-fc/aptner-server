package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.pinned.PinnedPostCommentImage;

public interface PinnedPostCommentImageRepository {

    PinnedPostCommentImage store(PinnedPostCommentImage pinnedPostCommentImage);

    PinnedPostCommentImage getByPinnedPostCommentId(Long commentId);

}
