package com.fc8.service;

import com.fc8.platform.dto.record.DisclosureInfo;
import com.fc8.platform.dto.record.NoticeInfo;
import com.fc8.platform.dto.record.PostInfo;
import com.fc8.platform.dto.record.QnaInfo;

import java.util.List;

public interface SearchService {
    List<PostInfo> searchPostList(Long memberId, String apartCode, String keyword, int pinnedPostCount);

    List<QnaInfo> searchQnaList(Long memberId, String apartCode, String keyword, int pinnedQnaCount);

    List<DisclosureInfo> searchDisclosureList(String apartCode, String keyword, int pinnedDisclosureCount);

    List<NoticeInfo> searchNoticeList(String apartCode, String keyword, int pinnedNoticeCount);
}
