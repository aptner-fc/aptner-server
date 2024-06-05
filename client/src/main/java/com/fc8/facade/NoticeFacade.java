package com.fc8.facade;

import com.fc8.platform.dto.record.NoticeDetailInfo;
import com.fc8.platform.dto.record.NoticeFileInfo;
import com.fc8.platform.dto.record.NoticeInfo;
import com.fc8.platform.dto.record.SearchPageCommand;
import com.fc8.platform.dto.response.LoadNoticeDetailResponse;
import com.fc8.platform.dto.response.LoadNoticeListResponse;
import com.fc8.platform.dto.response.PageResponse;
import com.fc8.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    @Transactional(readOnly = true)
    public PageResponse<LoadNoticeListResponse> loadNoticeList(Long memberId, String apartCode, SearchPageCommand command) {
        final Page<NoticeInfo> noticeList = noticeService.loadNoticeList(memberId, apartCode, command);
        return new PageResponse<>(noticeList, new LoadNoticeListResponse(noticeList.getContent()));
    }
}
