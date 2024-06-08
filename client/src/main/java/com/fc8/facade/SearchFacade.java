package com.fc8.facade;

import com.fc8.platform.dto.response.LoadUnifiedListResponse;
import com.fc8.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchFacade {

    private final SearchService searchService;

    @Transactional(readOnly = true)
    public LoadUnifiedListResponse search(Long memberId, String apartCode, String keyword) {
        // 1. 중요글 조회

        // 2. 게시글마다 5 - 중요글 개수 만큼 게시글 조회 (if(중요글 개수 == 5) 조회 x)

        return null;
    }
}
