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
    private final List<SearchNoticeInfo> pinnedNoticeList;
    private final List<SearchNoticeInfo> noticeList;

    private final Long disclosureCount;
    private final List<SearchDisclosureInfo> pinnedDisclosureList;
    private final List<SearchDisclosureInfo> disclosureList;

    private final Long postCount;
    private final List<SearchPostInfo> pinnedPostList;
    private final List<SearchPostInfo> postList;

    private final Long qnaCount;
    private final List<SearchQnaInfo> pinnedQnaList;
    private final List<SearchQnaInfo> qnaList;

    public LoadUnifiedListResponse(
        Long noticeCount, List<SearchNoticeInfo> pinnedNoticeList, List<SearchNoticeInfo> noticeList,
        Long disclosureCount, List<SearchDisclosureInfo> pinnedDisclosureList, List<SearchDisclosureInfo> disclosureList,
        Long postCount, List<SearchPostInfo> pinnedPostList, List<SearchPostInfo> postList,
        Long qnaCount, List<SearchQnaInfo> pinnedQnaList, List<SearchQnaInfo> qnaList
    ) {
        this.noticeCount = noticeCount;
        this.pinnedNoticeList = pinnedNoticeList;
        this.noticeList = noticeList;
        this.disclosureCount = disclosureCount;
        this.pinnedDisclosureList = pinnedDisclosureList;
        this.disclosureList = disclosureList;
        this.postCount = postCount;
        this.pinnedPostList = pinnedPostList;
        this.postList = postList;
        this.qnaCount = qnaCount;
        this.pinnedQnaList = pinnedQnaList;
        this.qnaList = qnaList;
    }
}
