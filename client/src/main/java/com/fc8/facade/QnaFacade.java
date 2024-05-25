package com.fc8.facade;

import com.fc8.platform.dto.command.CreateQnaCommand;
import com.fc8.platform.dto.record.QnaInfo;
import com.fc8.platform.dto.record.SearchPageCommand;
import com.fc8.platform.dto.response.CreateQnaResponse;
import com.fc8.platform.dto.response.LoadQnaListResponse;
import com.fc8.platform.dto.response.PageResponse;
import com.fc8.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaFacade {

    private final QnaService qnaService;

    public CreateQnaResponse create(Long memberId, String apartCode, CreateQnaCommand command, MultipartFile image) {
        return new CreateQnaResponse(qnaService.create(memberId, apartCode, command, image));
    }

    @Transactional(readOnly = true)
    public PageResponse<LoadQnaListResponse> loadQnaList(Long memberId, String apartCode, SearchPageCommand command) {
        final Page<QnaInfo> qnas = qnaService.loadQnaList(memberId, apartCode, command);
        // 상단 고정 게시물
        return new PageResponse<>(qnas, new LoadQnaListResponse(qnas.getContent(), null));
    }
}
