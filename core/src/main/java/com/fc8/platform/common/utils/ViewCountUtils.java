package com.fc8.platform.common.utils;

import com.fc8.platform.common.properties.RedisProperties;
import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.qna.Qna;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewCountUtils {

    private final RedisUtils redisUtils;

    public void increasePostViewCount(Post post) {
        redisUtils.increaseViewCountToRedis(RedisProperties.VIEW_POST, post.getId(), post.getViewCount());
    }

    public void increaseQnaViewCount(Qna qna) {
        redisUtils.increaseViewCountToRedis(RedisProperties.VIEW_QNA, qna.getId(), qna.getViewCount());
    }

    public void increaseDisclosureViewCount(Disclosure disclosure) {
        redisUtils.increaseViewCountToRedis(RedisProperties.VIEW_DISCLOSURE, disclosure.getId(), disclosure.getViewCount());
    }

    public void increaseNoticeViewCount(Notice notice) {
        redisUtils.increaseViewCountToRedis(RedisProperties.VIEW_NOTICE, notice.getId(), notice.getViewCount());
    }

    public void increasePinnedPostViewCount(PinnedPost pinnedPost) {
        redisUtils.increaseViewCountToRedis(RedisProperties.VIEW_PINNED_POST, pinnedPost.getId(), pinnedPost.getViewCount());
    }
}
