package com.fc8.facade;

import com.fc8.platform.dto.record.DisclosureInfo;
import com.fc8.platform.dto.record.NoticeInfo;
import com.fc8.platform.dto.record.PostInfo;
import com.fc8.platform.dto.record.QnaInfo;
import com.fc8.platform.dto.response.LoadUnifiedListResponse;
import com.fc8.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchFacade {

    private final SearchService searchService;

    @Transactional(readOnly = true)
    public LoadUnifiedListResponse search(Long memberId, String apartCode, String keyword) {
        // 1. 중요글 조회
        int pinnedPostCount = 2;
        int pinnedQnaCount = 2;
        int pinnedDisclosureCount = 2;
        int pinnedNoticeCount = 2;

        // 2. 게시글마다 5 - 중요글 개수 만큼 게시글 조회 (if(중요글 개수 == 5) 조회 x)
        List<PostInfo> postList = searchService.searchPostList(memberId, apartCode, keyword, pinnedPostCount);
        List<QnaInfo> qnaList = searchService.searchQnaList(memberId, apartCode, keyword, pinnedQnaCount);
        List<DisclosureInfo> disclosureList = searchService.searchDisclosureList(apartCode, keyword, pinnedDisclosureCount);
        List<NoticeInfo> noticeList = searchService.searchNoticeList(apartCode, keyword, pinnedNoticeCount);

        return new LoadUnifiedListResponse(null, noticeList, disclosureList, postList, qnaList);
    }
}
