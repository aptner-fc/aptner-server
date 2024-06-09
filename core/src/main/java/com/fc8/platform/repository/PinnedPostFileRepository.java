package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.pinned.PinnedPostFile;

import java.util.List;

public interface PinnedPostFileRepository {

    List<PinnedPostFile> getAllByPinnedPost(PinnedPost pinnedPost);
}
