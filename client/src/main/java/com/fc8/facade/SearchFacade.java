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

        // 2. 게시글 조회
        List<SearchNoticeInfo> noticeList = noticeService.searchNoticeList(apartCode, keyword, pinnedNoticeList.size());
        List<SearchDisclosureInfo> disclosureList = disclosureService.searchDisclosureList(apartCode, keyword, pinnedDisclosureList.size());
        List<SearchPostInfo> postList = postService.searchPostList(memberId, apartCode, keyword, pinnedPostList.size());
        List<SearchQnaInfo> qnaList = qnaService.searchQnaList(memberId, apartCode, keyword, pinnedQnaList.size());

        // 3. 게시글 총 개수 조회
        Long noticeCount = noticeService.getNoticeCount(apartCode, keyword);
        Long disclosureCount = disclosureService.getDisclosureCount(apartCode, keyword);
        Long postCount = postService.getPostCount(memberId, apartCode, keyword);
        Long QnaCount = qnaService.getQnaCount(memberId, apartCode, keyword);

        return new LoadUnifiedListResponse(
            noticeCount, disclosureCount, postCount, QnaCount,
            pinnedNoticeList, noticeList, pinnedDisclosureList, disclosureList, pinnedPostList, postList, pinnedQnaList, qnaList);
    }
}
