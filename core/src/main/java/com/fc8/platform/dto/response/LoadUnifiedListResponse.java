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
        List<SearchNoticeInfo> pinnedNoticeList,
        List<SearchNoticeInfo> noticeList,
        List<SearchDisclosureInfo> pinnedDisclosureList,
        List<SearchDisclosureInfo> disclosureList,
        List<SearchPostInfo> pinnedPostList,
        List<SearchPostInfo> postList,
        List<SearchQnaInfo> pinnedQnaList,
        List<SearchQnaInfo> qnaList
    ) {
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
