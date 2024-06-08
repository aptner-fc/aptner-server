package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.pinned.PinnedPostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PinnedPostCommentRepository {

    PinnedPostComment store(PinnedPostComment pinnedPostComment);

    Page<PinnedPostComment> getAllByPinnedPost(Long pinnedPostId, Pageable pageable);

    PinnedPostComment getByIdAndPinnedPost(Long id, PinnedPost pinnedPost);

    PinnedPostComment getByIdAndPinnedPostIdAndMemberId(Long id, Long pinnedPostId, Long memberId);
}
