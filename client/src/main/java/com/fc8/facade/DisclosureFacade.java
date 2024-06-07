package com.fc8.facade;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.dto.response.*;
import com.fc8.service.DisclosureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisclosureFacade {

    private final DisclosureService disclosureService;

    @Transactional(readOnly = true)
    public LoadDisclosureDetailResponse loadDisclosureDetail(Long memberId, Long disclosureId, String apartCode) {
        final DisclosureDetailInfo disclosureDetailInfo = disclosureService.loadDisclosureDetail(memberId, disclosureId, apartCode);
        final List<DisclosureFileInfo> disclosureFileList = disclosureService.loadDisclosureFileList(disclosureId, apartCode);
        return new LoadDisclosureDetailResponse(disclosureDetailInfo, disclosureFileList);
    }

    @Transactional(readOnly = true)
    public PageResponse<LoadDisclosureListResponse> loadDisclosureList(Long memberId, String apartCode, SearchPageCommand command) {
        final Page<DisclosureInfo> disclosureList = disclosureService.loadDisclosureList(memberId, apartCode, command);
        return new PageResponse<>(disclosureList, new LoadDisclosureListResponse(disclosureList.getContent()));
    }

    public PageResponse<LoadDisclosureCommentListResponse> loadCommentList(Long memberId, String apartCode, Long disclosureId, CustomPageCommand command) {
        final Page<DisclosureCommentInfo> commentList = disclosureService.loadCommentList(memberId, apartCode, disclosureId, command);
        return new PageResponse<>(commentList, new LoadDisclosureCommentListResponse(commentList.getContent()));
    }

    public RegisterEmojiResponse registerEmoji(Long memberId, Long disclosureId, String apartCode, EmojiType emoji) {
        return new RegisterEmojiResponse(disclosureService.registerEmoji(memberId, disclosureId, apartCode, emoji));
    }

    public void deleteEmoji(Long memberId, Long disclosureId, String apartCode, EmojiType emoji) {
        disclosureService.deleteEmoji(memberId, disclosureId, apartCode, emoji);
    }
}

