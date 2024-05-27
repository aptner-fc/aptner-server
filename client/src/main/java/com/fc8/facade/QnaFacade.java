package com.fc8.facade;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WriteQnaCommand;
import com.fc8.platform.dto.command.WriteQnaCommentCommand;
import com.fc8.platform.dto.record.QnaInfo;
import com.fc8.platform.dto.record.SearchPageCommand;
import com.fc8.platform.dto.response.*;
import com.fc8.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaFacade {

    private final QnaService qnaService;

    public WriteQnaResponse write(Long memberId, String apartCode, WriteQnaCommand command, MultipartFile image) {
        return new WriteQnaResponse(qnaService.create(memberId, apartCode, command, image));
    }

    @Transactional(readOnly = true)
    public PageResponse<LoadQnaListResponse> loadQnaList(Long memberId, String apartCode, SearchPageCommand command) {
        final Page<QnaInfo> qnaList = qnaService.loadQnaList(memberId, apartCode, command);
        // 상단 고정 게시물
        return new PageResponse<>(qnaList, new LoadQnaListResponse(qnaList.getContent(), null));
    }

    public DeleteQnaResponse deleteQna(Long memberId, Long qnaId, String apartCode) {
        return new DeleteQnaResponse(qnaService.deleteQna(memberId, qnaId, apartCode));
    }

    public LoadQnaDetailResponse loadQnaDetail(Long memberId, Long qnaId, String apartCode) {
        return new LoadQnaDetailResponse(qnaService.loadQnaDetail(memberId, qnaId, apartCode));
    }

    public RegisterEmojiResponse registerEmoji(Long memberId, Long qnaId, String apartCode, EmojiType emoji) {
        return new RegisterEmojiResponse(qnaService.registerEmoji(memberId, qnaId, apartCode, emoji));
    }

    public void deleteEmoji(Long memberId, Long qnaId, String apartCode, EmojiType emoji) {
        qnaService.deleteEmoji(memberId, qnaId, apartCode, emoji);
    }

    public WriteQnaCommentResponse writeComment(Long memberId, Long qnaId, String apartCode, WriteQnaCommentCommand command, MultipartFile image) {
        return new WriteQnaCommentResponse(
            Optional.ofNullable(command.getParentId())
                .map(parentId -> qnaService.writeReply(memberId, qnaId, apartCode, command, image))
                .orElseGet(() -> qnaService.writeComment(memberId, qnaId, apartCode, command, image))
        );
    }

    public DeleteQnaCommentResponse deleteComment(Long memberId, Long qnaId, Long qnaCommentId, String apartCode) {
        return new DeleteQnaCommentResponse(qnaService.deleteComment(memberId, qnaId, qnaCommentId, apartCode));
    }
}
