package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.NoticeInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadNoticeListResponse {

    List<NoticeInfo> noticeInfoList;

    public LoadNoticeListResponse(List<NoticeInfo> noticeInfoList) {
        this.noticeInfoList = noticeInfoList;
    }
}
