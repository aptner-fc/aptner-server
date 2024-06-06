package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.*;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadUnifiedListResponse {

//    List<PinnedPost> pinnedPostList;
    List<QnaInfo> qnaList;
    List<PostInfo> postList;
    List<NoticeInfo> noticeList;
//    List<의무공개Info> 의무공개List;

    public LoadUnifiedListResponse(List<QnaInfo> qnaList, List<PostInfo> postList, List<NoticeInfo> noticeList) {
        this.qnaList = qnaList;
        this.postList = postList;
        this.noticeList = noticeList;
    }
}
