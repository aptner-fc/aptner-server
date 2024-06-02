package com.fc8.facade;

import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.command.WriteQnaCommand;
import com.fc8.platform.dto.command.WriteQnaCommentCommand;
import com.fc8.platform.dto.record.*;
import com.fc8.platform.dto.response.*;
import com.fc8.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaFacade {

    private final QnaService qnaService;

    public WriteQnaResponse writeQna(Long memberId, String apartCode, WriteQnaCommand command, List<MultipartFile> files) {
        return new WriteQnaResponse(qnaService.writeQna(memberId, apartCode, command, files));
    }

    public WriteQnaResponse modifyQna(Long memberId, Long qnaId, String apartCode, WriteQnaCommand command, MultipartFile image) {
        return new WriteQnaResponse(qnaService.modifyQna(memberId, qnaId, apartCode, command, image));
    }

    public DeleteQnaResponse deleteQna(Long memberId, Long qnaId, String apartCode) {
        return new DeleteQnaResponse(qnaService.deleteQna(memberId, qnaId, apartCode));
    }

    @Transactional(readOnly = true)
    public LoadQnaDetailResponse loadQnaDetail(Long memberId, Long qnaId, String apartCode) {
        final QnaDetailInfo qnaDetailInfo = qnaService.loadQnaDetail(memberId, qnaId, apartCode);
        final List<QnaFileInfo> qnaFileList = qnaService.loadQnaFileList(qnaId, apartCode);
        return new LoadQnaDetailResponse(qnaDetailInfo, qnaFileList);
    }

    @Transactional(readOnly = true)
    public PageResponse<LoadQnaListResponse> loadQnaList(Long memberId, String apartCode, SearchPageCommand command) {
        final Page<QnaInfo> qnaList = qnaService.loadQnaList(memberId, apartCode, command);
        // 상단 고정 게시물
        return new PageResponse<>(qnaList, new LoadQnaListResponse(qnaList.getContent(), null));
    }

    public WriteQnaCommentResponse writeComment(Long memberId, Long qnaId, String apartCode, WriteQnaCommentCommand command, MultipartFile image) {
        return new WriteQnaCommentResponse(
            Optional.ofNullable(command.getParentId())
                .map(parentId -> qnaService.writeReply(memberId, qnaId, apartCode, command, image))
                .orElseGet(() -> qnaService.writeComment(memberId, qnaId, apartCode, command, image))
        );
    }

    public WriteQnaCommentResponse modifyComment(Long memberId, Long qnaId, Long commentId, String apartCode, WriteQnaCommentCommand command, MultipartFile image) {
        return new WriteQnaCommentResponse(qnaService.modifyComment(memberId, qnaId, commentId, apartCode, command, image));
    }

    public DeleteQnaCommentResponse deleteComment(Long memberId, Long qnaId, Long qnaCommentId, String apartCode) {
        return new DeleteQnaCommentResponse(qnaService.deleteComment(memberId, qnaId, qnaCommentId, apartCode));
    }

    public PageResponse<LoadQnaCommentListResponse> loadCommentList(Long memberId, String apartCode, Long qnaId, CustomPageCommand command) {
        final Page<QnaCommentInfo> commentList = qnaService.loadCommentList(memberId, apartCode, qnaId, command);
        return new PageResponse<>(commentList, new LoadQnaCommentListResponse(commentList.getContent()));
    }

    public RegisterEmojiResponse registerEmoji(Long memberId, Long qnaId, String apartCode, EmojiType emoji) {
        return new RegisterEmojiResponse(qnaService.registerEmoji(memberId, qnaId, apartCode, emoji));
    }

    public void deleteEmoji(Long memberId, Long qnaId, String apartCode, EmojiType emoji) {
        qnaService.deleteEmoji(memberId, qnaId, apartCode, emoji);
    }

}
