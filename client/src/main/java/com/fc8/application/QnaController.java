package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentMember;
import com.fc8.facade.QnaFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.CurrentMember;
import com.fc8.platform.dto.request.SearchPageRequest;
import com.fc8.platform.dto.request.WriteQnaCommentRequest;
import com.fc8.platform.dto.request.WriteQnaRequest;
import com.fc8.platform.dto.response.*;
import com.fc8.platform.mapper.PageMapper;
import com.fc8.platform.mapper.QnaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "민원 게시판 관련 API")
@RestController
@RequestMapping(value = {"/v1/api/qna"})
@RequiredArgsConstructor
public class QnaController {

    private final QnaMapper qnaMapper;
    private final PageMapper pageMapper;
    private final QnaFacade qnaFacade;

    @Operation(summary = "민원 등록 API")
    @CheckApartType
    @PostMapping(value = "/{apartCode}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WriteQnaResponse>> writeQna(
        @NotNull @PathVariable String apartCode,
        @CheckCurrentMember CurrentMember currentMember,
        @Valid @RequestPart(value = "request") WriteQnaRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        var command = qnaMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, qnaFacade.writeQna(currentMember.id(), apartCode, command, image));
    }

    @Operation(summary = "민원 수정 API")
    @CheckApartType
    @PatchMapping(value = "/{apartCode}/{qnaId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WriteQnaResponse>> modifyQna(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long qnaId,
        @CheckCurrentMember CurrentMember currentMember,
        @Valid @RequestPart(value = "request") WriteQnaRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image) {
        var command = qnaMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE, qnaFacade.modifyQna(currentMember.id(), qnaId, apartCode, command, image));
    }

    @Operation(summary = "민원 삭제 API")
    @CheckApartType
    @DeleteMapping("/{apartCode}/{qnaId}")
    public ResponseEntity<CommonResponse<DeleteQnaResponse>> deleteQna(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long qnaId,
        @CheckCurrentMember CurrentMember currentMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE, qnaFacade.deleteQna(currentMember.id(), qnaId, apartCode));
    }

    @Operation(summary = "민원 상세 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/{qnaId}")
    public ResponseEntity<CommonResponse<LoadQnaDetailResponse>> loadQnaDetail(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long qnaId,
        @CheckCurrentMember CurrentMember currentMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, qnaFacade.loadQnaDetail(currentMember.id(), qnaId, apartCode));
    }

    @Operation(summary = "민원 게시판 목록 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}")
    public ResponseEntity<CommonResponse<PageResponse<LoadQnaListResponse>>> loadQnaList(
        @NotNull @PathVariable String apartCode,
        @CheckCurrentMember CurrentMember currentMember,
        SearchPageRequest request
    ) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, qnaFacade.loadQnaList(currentMember.id(), apartCode, command));
    }

    @Operation(summary = "민원 게시판 댓글 등록 API")
    @CheckApartType
    @PostMapping(value = "/{apartCode}/{qnaId}/comments", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WriteQnaCommentResponse>> writeComment(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long qnaId,
        @CheckCurrentMember CurrentMember currentMember,
        @Valid @RequestPart(value = "request") WriteQnaCommentRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        var command = qnaMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, qnaFacade.writeComment(currentMember.id(), qnaId, apartCode, command, image));
    }

    @Operation(summary = "민원 게시판 댓글 수정 API")
    @CheckApartType
    @PatchMapping(value = "/{apartCode}/{qnaId}/comments/{commentId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WriteQnaCommentResponse>> modifyComment(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long qnaId,
        @NotNull @PathVariable Long commentId,
        @CheckCurrentMember CurrentMember currentMember,
        @Valid @RequestPart(value = "request") WriteQnaCommentRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        var command = qnaMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE, qnaFacade.modifyComment(currentMember.id(), qnaId, commentId, apartCode, command, image));
    }

    @Operation(summary = "민원 게시판 댓글 삭제 API")
    @CheckApartType
    @DeleteMapping(value = "/{apartCode}/{qnaId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<DeleteQnaCommentResponse>> deleteComment(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long qnaId,
        @NotNull @PathVariable Long commentId,
        @CheckCurrentMember CurrentMember currentMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE, qnaFacade.deleteComment(currentMember.id(), qnaId, commentId, apartCode));
    }

    @Operation(summary = "민원 게시판 댓글 목록 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/{qnaId}/comments")
    public ResponseEntity<CommonResponse<PageResponse<LoadQnaCommentListResponse>>> loadCommentList(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long qnaId,
        @CheckCurrentMember CurrentMember currentMember,
        SearchPageRequest request
    ) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, qnaFacade.loadCommentList(currentMember.id(), apartCode, qnaId, command));
    }

    @Operation(summary = "민원 게시판 이모지 등록 API")
    @CheckApartType
    @PostMapping("/{apartCode}/{qnaId}/emoji")
    public ResponseEntity<CommonResponse<RegisterEmojiResponse>> registerEmoji(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long qnaId,
        @CheckCurrentMember CurrentMember currentMember,
        @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, qnaFacade.registerEmoji(currentMember.id(), qnaId, apartCode, type));
    }

    @Operation(summary = "민원 게시판 이모지 삭제 API")
    @CheckApartType
    @DeleteMapping("/{apartCode}/{qnaId}/emoji")
    public ResponseEntity<CommonResponse<Void>> deleteEmoji(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long qnaId,
        @CheckCurrentMember CurrentMember currentMember,
        @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type
    ) {
        qnaFacade.deleteEmoji(currentMember.id(), qnaId, apartCode, type);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

}
