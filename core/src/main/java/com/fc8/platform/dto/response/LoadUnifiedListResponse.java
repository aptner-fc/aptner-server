package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.*;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadUnifiedListResponse {

    // List<PinnedPost> pinnedPostList;
    List<QnaInfo> qnaList;
    List<PostInfo> postList;
    List<NoticeInfo> noticeList;
    List<DisclosureInfo> disclosureList;

    public LoadUnifiedListResponse(List<PinnedPost> pinnedPostList, List<QnaInfo> qnaList, List<PostInfo> postList, List<NoticeInfo> noticeList, List<DisclosureInfo> disclosureList) {
//        this.pinnedPostList = pinnedPostList;
        this.qnaList = qnaList;
        this.postList = postList;
        this.noticeList = noticeList;
        this.disclosureList = disclosureList;
    }
}
