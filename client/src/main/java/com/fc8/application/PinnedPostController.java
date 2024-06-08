package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentMember;
import com.fc8.facade.PinnedPostFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.CurrentMember;
import com.fc8.platform.dto.request.CustomPageRequest;
import com.fc8.platform.dto.request.WritePostCommentRequest;
import com.fc8.platform.dto.response.*;
import com.fc8.platform.mapper.PageMapper;
import com.fc8.platform.mapper.PostMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "중요글 관련 API")
@RestController
@RequestMapping(value = {"/v1/api/pinned-post"})
@RequiredArgsConstructor
public class PinnedPostController {

    private final PinnedPostFacade pinnedPostFacade;
    private final PageMapper pageMapper;
    private final PostMapper postMapper;

    @Operation(summary = "중요글 상세 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/{categoryCode}/{pinnedPostId}")
    public ResponseEntity<CommonResponse<LoadPinnedPostDetailResponse>> loadPinnedPostDetail(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable String categoryCode,
        @NotNull @PathVariable Long pinnedPostId,
        @CheckCurrentMember CurrentMember currentMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, pinnedPostFacade.loadPinnedPostDetail(currentMember.id(), apartCode, categoryCode, pinnedPostId));
    }
    @Operation(summary = "중요글 댓글 등록 API")
    @CheckApartType
    @PostMapping(value = "/{apartCode}/{pinnedPostId}/comments", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WritePostCommentResponse>> writeComment(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long pinnedPostId,
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestPart(value = "request") WritePostCommentRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        var command = postMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, pinnedPostFacade.writeComment(currentMember.id(), apartCode, pinnedPostId, command, image));
    }

    @Operation(summary = "중요글 댓글 수정 API")
    @CheckApartType
    @PatchMapping(value = "/{apartCode}/{pinnedPostId}/comments/{commentId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WritePostCommentResponse>> modifyComment(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long pinnedPostId,
            @NotNull @PathVariable Long commentId,
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestPart(value = "request") WritePostCommentRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        var command = postMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, pinnedPostFacade.modifyComment(currentMember.id(), apartCode, pinnedPostId, commentId, command, image));
    }

    @Operation(summary = "중요글 댓글 목록 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/{pinnedPostId}/comments")
    public ResponseEntity<CommonResponse<PageResponse<LoadPinnedPostCommentListResponse>>> loadPinnedPostCommentList(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long pinnedPostId,
        @CheckCurrentMember CurrentMember currentMember,
        CustomPageRequest request
    ) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, pinnedPostFacade.loadPinnedPostCommentList(currentMember.id(), apartCode, pinnedPostId, command));
    }
//
    @Operation(summary = "중요글 이모지 등록 API")
    @CheckApartType
    @PostMapping("/{apartCode}/{pinnedPostId}/emoji")
    public ResponseEntity<CommonResponse<RegisterEmojiResponse>> registerEmoji(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long pinnedPostId,
        @CheckCurrentMember CurrentMember currentMember,
        @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, pinnedPostFacade.registerEmoji(currentMember.id(), apartCode, pinnedPostId, type));
    }

    @Operation(summary = "중요글 이모지 삭제 API")
    @CheckApartType
    @DeleteMapping("/{apartCode}/{pinnedPostId}/emoji")
    public ResponseEntity<CommonResponse<Void>> deleteEmoji(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long pinnedPostId,
        @CheckCurrentMember CurrentMember currentMember,
        @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type
    ) {
        pinnedPostFacade.deleteEmoji(currentMember.id(), apartCode, pinnedPostId, type);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }


}
