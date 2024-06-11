package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.NoticeInfo;
import com.fc8.platform.dto.record.PinnedPostInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadNoticeListResponse {

    private final List<NoticeInfo> noticeInfoList;
    private final List<PinnedPostInfo> pinnedNoticeList;

    public LoadNoticeListResponse(List<NoticeInfo> noticeInfoList, List<PinnedPostInfo> pinnedNoticeList) {
        this.noticeInfoList = noticeInfoList;
        this.pinnedNoticeList = pinnedNoticeList;
    }
}
