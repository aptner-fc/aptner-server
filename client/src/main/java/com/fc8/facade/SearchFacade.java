package com.fc8.facade;

import com.fc8.platform.dto.record.*;
import com.fc8.platform.dto.response.LoadUnifiedListResponse;
import com.fc8.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchFacade {

    private final PinnedPostService pinnedPostService;
    private final QnaService qnaService;
    private final NoticeService noticeService;
    private final PostService postService;
    private final DisclosureService disclosureService;

    @Transactional(readOnly = true)
    public LoadUnifiedListResponse search(Long memberId, String apartCode, String keyword) {
        // 1. 중요글 조회
        List<SearchNoticeInfo> pinnedNoticeList = pinnedPostService.searchPinnedNoticeList(apartCode, keyword, "NT000");
        List<SearchDisclosureInfo> pinnedDisclosureList = pinnedPostService.searchPinnedDisclosureList(apartCode, keyword, "DC000");
        List<SearchPostInfo> pinnedPostList = pinnedPostService.searchPinnedPostList(apartCode, keyword, "PT000");
        List<SearchQnaInfo> pinnedQnaList = pinnedPostService.searchPinnedQnaList(apartCode, keyword, "QA000");

        // 2. 게시글마다 5 - 중요글 개수만큼 게시글 조회
        List<SearchNoticeInfo> noticeList = noticeService.searchNoticeList(apartCode, keyword, pinnedNoticeList.size(), "NT000");
        List<SearchDisclosureInfo> disclosureList = disclosureService.searchDisclosureList(apartCode, keyword, pinnedDisclosureList.size(), "DC000");
        List<SearchPostInfo> postList = postService.searchPostList(memberId, apartCode, keyword, pinnedPostList.size(), "PT000");
        List<SearchQnaInfo> qnaList = qnaService.searchQnaList(memberId, apartCode, keyword, pinnedQnaList.size(), "QA000");

        return new LoadUnifiedListResponse(
            pinnedNoticeList, noticeList, pinnedDisclosureList, disclosureList, pinnedPostList, postList, pinnedQnaList, qnaList);
    }
}
