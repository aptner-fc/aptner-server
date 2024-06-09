package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.NoticeDetailInfo;
import com.fc8.platform.dto.record.NoticeFileInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadNoticeDetailResponse {

    private final NoticeDetailInfo noticeDetailInfo;
    private final List<NoticeFileInfo> noticeFileInfoList;

    public LoadNoticeDetailResponse(NoticeDetailInfo noticeDetailInfo, List<NoticeFileInfo> noticeFileInfoList) {
        this.noticeDetailInfo = noticeDetailInfo;
        this.noticeFileInfoList = noticeFileInfoList;
    }
}
