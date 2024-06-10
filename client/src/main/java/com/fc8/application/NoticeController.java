package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentMember;
import com.fc8.facade.NoticeFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.CurrentMember;
import com.fc8.platform.dto.request.CustomPageRequest;
import com.fc8.platform.dto.request.SearchPageRequest;
import com.fc8.platform.dto.request.WriteNoticeCommentRequest;
import com.fc8.platform.dto.response.*;
import com.fc8.platform.mapper.NoticeMapper;
import com.fc8.platform.mapper.PageMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "공지사항 관련 API")
@RestController
@RequestMapping(value = {"/v1/api/notices"})
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeFacade noticeFacade;
    private final PageMapper pageMapper;
    private final NoticeMapper noticeMapper;

    @Operation(summary = "공지사항 상세 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/{noticeId}")
    public ResponseEntity<CommonResponse<LoadNoticeDetailResponse>> loadNoticeDetail(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long noticeId,
        @CheckCurrentMember CurrentMember currentMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, noticeFacade.loadNoticeDetail(currentMember.id(), noticeId, apartCode));
    }

    @Operation(summary = "공지사항 목록 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}")
    public ResponseEntity<CommonResponse<PageResponse<LoadNoticeListResponse>>> loadNoticeList(
        @NotNull @PathVariable String apartCode,
        @CheckCurrentMember CurrentMember currentMember,
        SearchPageRequest request
    ) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, noticeFacade.loadNoticeList(currentMember.id(), apartCode, command));
    }

    @Operation(summary = "공지사항 댓글 등록 API")
    @CheckApartType
    @PostMapping(value = "/{apartCode}/{noticeId}/comments", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WriteNoticeCommentResponse>> writeComment(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long noticeId,
        @CheckCurrentMember CurrentMember currentMember,
        @Valid @RequestPart(value = "request") WriteNoticeCommentRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        var command = noticeMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, noticeFacade.writeComment(currentMember.id(), noticeId, apartCode, command, image));
    }

//    @Operation(summary = "공지사항 댓글 수정 API")
//    @CheckApartType
//    @PatchMapping(value = "/{apartCode}/{noticeId}/comments/{commentId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<CommonResponse<WriteNoticeCommentResponse>> modifyComment(
//        @NotNull @PathVariable String apartCode,
//        @NotNull @PathVariable Long noticeId,
//        @NotNull @PathVariable Long commentId,
//        @CheckCurrentMember CurrentMember currentMember,
//        @Valid @RequestPart(value = "request") WriteNoticeCommentRequest request,
//        @RequestPart(value = "image", required = false) MultipartFile image
//    ) {
//        var command = noticeMapper.of(request);
//        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE, noticeFacade.modifyComment(currentMember.id(), noticeId, commentId, apartCode, command, image));
//    }
//
//    @Operation(summary = "공지사항 댓글 삭제 API")
//    @CheckApartType
//    @DeleteMapping(value = "/{apartCode}/{noticeId}/comments/{commentId}")
//    public ResponseEntity<CommonResponse<DeleteNoticeCommentResponse>> deleteComment(
//        @NotNull @PathVariable String apartCode,
//        @NotNull @PathVariable Long noticeId,
//        @NotNull @PathVariable Long commentId,
//        @CheckCurrentMember CurrentMember currentMember
//    ) {
//        return CommonResponse.success(SuccessCode.SUCCESS_DELETE, noticeFacade.deleteComment(currentMember.id(), noticeId, commentId, apartCode));
//    }


    @Operation(summary = "공지사항 댓글 목록 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/{noticeId}/comments")
    public ResponseEntity<CommonResponse<PageResponse<LoadNoticeCommentListResponse>>> loadCommentList(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long noticeId,
        @CheckCurrentMember CurrentMember currentMember,
        CustomPageRequest request
    ) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, noticeFacade.loadCommentList(currentMember.id(), apartCode, noticeId, command));
    }

    @Operation(summary = "공지사항 이모지 등록 API")
    @CheckApartType
    @PostMapping("/{apartCode}/{noticeId}/emoji")
    public ResponseEntity<CommonResponse<RegisterEmojiResponse>> registerEmoji(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long noticeId,
        @CheckCurrentMember CurrentMember currentMember,
        @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, noticeFacade.registerEmoji(currentMember.id(), noticeId, apartCode, type));
    }

    @Operation(summary = "공지사항 이모지 삭제 API")
    @CheckApartType
    @DeleteMapping("/{apartCode}/{noticeId}/emoji")
    public ResponseEntity<CommonResponse<Void>> deleteEmoji(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long noticeId,
        @CheckCurrentMember CurrentMember currentMember,
        @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type
    ) {
        noticeFacade.deleteEmoji(currentMember.id(), noticeId, apartCode, type);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

}
