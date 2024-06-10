package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.SearchDisclosureInfo;
import com.fc8.platform.dto.record.SearchNoticeInfo;
import com.fc8.platform.dto.record.SearchPostInfo;
import com.fc8.platform.dto.record.SearchQnaInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadUnifiedListResponse {

    private final Long noticeCount;
    private final Long disclosureCount;
    private final Long postCount;
    private final Long qnaCount;
    private final List<SearchNoticeInfo> pinnedNoticeList;
    private final List<SearchNoticeInfo> noticeList;
    private final List<SearchDisclosureInfo> pinnedDisclosureList;
    private final List<SearchDisclosureInfo> disclosureList;
    private final List<SearchPostInfo> pinnedPostList;
    private final List<SearchPostInfo> postList;
    private final List<SearchQnaInfo> pinnedQnaList;
    private final List<SearchQnaInfo> qnaList;

    public LoadUnifiedListResponse(
        Long noticeCount, Long disclosureCount, Long postCount, Long qnaCount,
        List<SearchNoticeInfo> pinnedNoticeList,
        List<SearchNoticeInfo> noticeList,
        List<SearchDisclosureInfo> pinnedDisclosureList,
        List<SearchDisclosureInfo> disclosureList,
        List<SearchPostInfo> pinnedPostList,
        List<SearchPostInfo> postList,
        List<SearchQnaInfo> pinnedQnaList,
        List<SearchQnaInfo> qnaList
    ) {
        this.noticeCount = noticeCount;
        this.disclosureCount = disclosureCount;
        this.postCount = postCount;
        this.qnaCount = qnaCount;
        this.pinnedNoticeList = pinnedNoticeList;
        this.noticeList = noticeList;
        this.pinnedDisclosureList = pinnedDisclosureList;
        this.disclosureList = disclosureList;
        this.pinnedPostList = pinnedPostList;
        this.postList = postList;
        this.pinnedQnaList = pinnedQnaList;
        this.qnaList = qnaList;
    }
}
