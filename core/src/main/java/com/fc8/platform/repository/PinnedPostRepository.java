package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.dto.record.PinnedPostInfo;

import java.util.List;

public interface PinnedPostRepository {

    PinnedPost getPinnedPostByIdAndApartCode(Long memberId, Long pinnedPostId, String apartCode);

    PinnedPost getByIdAndApartCode(Long pinnedPostId, String apartCode);

    List<PinnedPostInfo> getAllByApartCodeAndCategoryCode(String apartCode, String categoryCode);

    List<PinnedPost> getPinnedBulletinListByKeyword(String apartCode, String keyword, String categoryCode);
}
