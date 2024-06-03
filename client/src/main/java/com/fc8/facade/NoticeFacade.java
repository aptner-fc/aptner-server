package com.fc8.facade;

import com.fc8.platform.dto.record.NoticeDetailInfo;
import com.fc8.platform.dto.record.NoticeFileInfo;
import com.fc8.platform.dto.response.LoadNoticeDetailResponse;
import com.fc8.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeFacade {

    private final NoticeService noticeService;

    @Transactional(readOnly = true)
    public LoadNoticeDetailResponse loadNoticeDetail(Long memberId, Long noticeId, String apartCode) {
        final NoticeDetailInfo noticeDetailInfo = noticeService.loadNoticeDetail(memberId, noticeId, apartCode);
        final List<NoticeFileInfo> noticeFileList = noticeService.loadNoticeFileList(noticeId, apartCode);
        return new LoadNoticeDetailResponse(noticeDetailInfo, noticeFileList);
    }
}
