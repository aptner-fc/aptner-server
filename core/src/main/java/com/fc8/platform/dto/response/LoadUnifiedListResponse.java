package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.*;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadUnifiedListResponse {

    List<PinnedPostQueryInfo> pinnedPostList;
    List<NoticeInfo> noticeList;
    List<DisclosureInfo> disclosureList;
    List<PostInfo> postList;
    List<QnaInfo> qnaList;

    public LoadUnifiedListResponse(List<PinnedPostQueryInfo> pinnedPostList, List<NoticeInfo> noticeList, List<DisclosureInfo> disclosureList, List<PostInfo> postList, List<QnaInfo> qnaList) {
        this.pinnedPostList = pinnedPostList;
        this.noticeList = noticeList;
        this.disclosureList = disclosureList;
        this.postList = postList;
        this.qnaList = qnaList;
    }
}
