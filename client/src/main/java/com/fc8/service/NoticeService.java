package com.fc8.service;

import com.fc8.platform.dto.record.NoticeDetailInfo;
import com.fc8.platform.dto.record.NoticeFileInfo;
import com.fc8.platform.dto.record.NoticeInfo;
import com.fc8.platform.dto.record.SearchPageCommand;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NoticeService {
    NoticeDetailInfo loadNoticeDetail(Long memberId, Long noticeId, String apartCode);

    List<NoticeFileInfo> loadNoticeFileList(Long noticeId, String apartCode);

    Page<NoticeInfo> loadNoticeList(Long memberId, String apartCode, SearchPageCommand command);
}
