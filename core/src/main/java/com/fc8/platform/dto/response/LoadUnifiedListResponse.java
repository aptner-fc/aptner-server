package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.SearchDisclosureInfo;
import com.fc8.platform.dto.record.SearchNoticeInfo;
import com.fc8.platform.dto.record.SearchPostInfo;
import com.fc8.platform.dto.record.SearchQnaInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadUnifiedListResponse {

    List<SearchNoticeInfo> pinnedNoticeList;
    List<SearchNoticeInfo> noticeList;
    List<SearchDisclosureInfo> pinnedDisclosureList;
    List<SearchDisclosureInfo> disclosureList;
    List<SearchPostInfo> pinnedPostList;
    List<SearchPostInfo> postList;
    List<SearchQnaInfo> pinnedQnaList;
    List<SearchQnaInfo> qnaList;

    public LoadUnifiedListResponse(
        List<SearchNoticeInfo> noticeList,
        List<SearchNoticeInfo> pinnedNoticeList,
        List<SearchDisclosureInfo> disclosureList,
        List<SearchDisclosureInfo> pinnedDisclosureList,
        List<SearchPostInfo> postList,
        List<SearchPostInfo> pinnedPostList,
        List<SearchQnaInfo> qnaList,
        List<SearchQnaInfo> pinnedQnaList
    ) {
        this.noticeList = noticeList;
        this.pinnedNoticeList = pinnedNoticeList;
        this.disclosureList = disclosureList;
        this.pinnedDisclosureList = pinnedDisclosureList;
        this.postList = postList;
        this.pinnedPostList = pinnedPostList;
        this.qnaList = qnaList;
        this.pinnedQnaList = pinnedQnaList;
    }
}
