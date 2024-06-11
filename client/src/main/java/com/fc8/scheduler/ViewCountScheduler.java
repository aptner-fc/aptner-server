package com.fc8.scheduler;

import com.fc8.platform.common.properties.RedisProperties;
import com.fc8.platform.common.utils.RedisUtils;
import com.fc8.service.DisclosureService;
import com.fc8.service.NoticeService;
import com.fc8.service.PostService;
import com.fc8.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountScheduler {

    private final RedisUtils redisUtils;

    private final PostService postService;
    private final QnaService qnaService;
    private final NoticeService noticeService;
    private final DisclosureService disclosureService;

    @Transactional
    @Scheduled(cron = "0 */5 * * * *")
    public void deletePostViewCountCacheFromRedis() {
        Set<String> viewCountKeys = redisUtils.getKeys(RedisProperties.VIEW_POST + RedisProperties.VIEW_ASTERISK);

        if (Objects.requireNonNull(viewCountKeys).isEmpty()) {
            return;
        }

        for (String viewCountKey : viewCountKeys) {
            Long domainId = getDomainIdByRedisKey(viewCountKey);
            Long viewCount = Long.parseLong(String.valueOf(redisUtils.getViewCountValueByKey(viewCountKey)));

            // DB 데이터 반영
            postService.updateViewCount(domainId, viewCount);

            // 캐시 데이터 삭제
            redisUtils.deleteByKey(viewCountKey);
            redisUtils.deleteByKey(RedisProperties.VIEW_POST + "::" +domainId);
        }
    }

    @Transactional
    @Scheduled(cron = "0 */5 * * * *")
    public void deleteQnaViewCountCacheFromRedis() {
        Set<String> viewCountKeys = redisUtils.getKeys(RedisProperties.VIEW_QNA + RedisProperties.VIEW_ASTERISK);

        if (Objects.requireNonNull(viewCountKeys).isEmpty()) {
            return;
        }

        for (String viewCountKey : viewCountKeys) {
            Long domainId = getDomainIdByRedisKey(viewCountKey);
            Long viewCount = Long.parseLong(String.valueOf(redisUtils.getViewCountValueByKey(viewCountKey)));

            // DB 데이터 반영
            qnaService.updateViewCount(domainId, viewCount);

            // 캐시 데이터 삭제
            redisUtils.deleteByKey(viewCountKey);
            redisUtils.deleteByKey(RedisProperties.VIEW_QNA + "::" + domainId);
        }
    }

    @Transactional
    @Scheduled(cron = "0 */5 * * * *")
    public void deleteNoticeViewCountCacheFromRedis() {
        Set<String> viewCountKeys = redisUtils.getKeys(RedisProperties.VIEW_NOTICE + RedisProperties.VIEW_ASTERISK);

        if (Objects.requireNonNull(viewCountKeys).isEmpty()) {
            return;
        }

        for (String viewCountKey : viewCountKeys) {
            Long domainId = getDomainIdByRedisKey(viewCountKey);
            Long viewCount = Long.parseLong(String.valueOf(redisUtils.getViewCountValueByKey(viewCountKey)));

            // DB 데이터 반영
            noticeService.updateViewCount(domainId, viewCount);

            // 캐시 데이터 삭제
            redisUtils.deleteByKey(viewCountKey);
            redisUtils.deleteByKey(RedisProperties.VIEW_NOTICE + "::" + domainId);
        }
    }

    @Transactional
    @Scheduled(cron = "0 */5 * * * *")
    public void deleteDisclosureViewCountCacheFromRedis() {
        Set<String> viewCountKeys = redisUtils.getKeys(RedisProperties.VIEW_DISCLOSURE + RedisProperties.VIEW_ASTERISK);

        if (Objects.requireNonNull(viewCountKeys).isEmpty()) {
            return;
        }

        for (String viewCountKey : viewCountKeys) {
            Long domainId = getDomainIdByRedisKey(viewCountKey);
            Long viewCount = Long.parseLong(String.valueOf(redisUtils.getViewCountValueByKey(viewCountKey)));

            // DB 데이터 반영
            disclosureService.updateViewCount(domainId, viewCount);

            // 캐시 데이터 삭제
            redisUtils.deleteByKey(viewCountKey);
            redisUtils.deleteByKey(RedisProperties.VIEW_DISCLOSURE + "::" + domainId);
        }
    }

    private long getDomainIdByRedisKey(String viewCountKey) {
        return Long.parseLong(viewCountKey.split("::")[1]);
    }
}
