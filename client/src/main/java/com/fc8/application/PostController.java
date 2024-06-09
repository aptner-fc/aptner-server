package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentMember;
import com.fc8.facade.PostFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.CurrentMember;
import com.fc8.platform.dto.request.CustomPageRequest;
import com.fc8.platform.dto.request.SearchPageRequest;
import com.fc8.platform.dto.request.WritePostCommentRequest;
import com.fc8.platform.dto.request.WritePostRequest;
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

import java.util.List;

@Tag(name = "소통 게시판 관련 API")
@RestController
@RequestMapping(value = {"/v1/api/posts"})
@RequiredArgsConstructor
public class PostController {

    private final PostMapper postMapper;
    private final PageMapper pageMapper;
    private final PostFacade postFacade;

    @Operation(summary = "소통 게시판 글 작성 API")
    @CheckApartType
    @PostMapping(value = "/{apartCode}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WritePostResponse>> writePost(
            @NotNull @PathVariable String apartCode,
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestPart(value = "request") WritePostRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "files", required = false)List<MultipartFile> files
            ) {
        var command = postMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, postFacade.writePost(currentMember.id(), apartCode, command, image, files));
    }

    @Operation(summary = "소통 게시판 글 수정 API")
    @CheckApartType
    @PatchMapping(value = "/{apartCode}/{postId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WritePostResponse>> modifyPost(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long postId,
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestPart(value = "request") WritePostRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        var command = postMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, postFacade.modifyPost(currentMember.id(), postId, apartCode, command, image));
    }

    @Operation(summary = "소통 게시판 글 삭제 API")
    @CheckApartType
    @DeleteMapping(value = "/{apartCode}/{postId}")
    public ResponseEntity<CommonResponse<DeletePostResponse>> deletePost(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long postId,
        @CheckCurrentMember CurrentMember currentMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE, postFacade.deletePost(currentMember.id(), postId, apartCode));
    }

    @Operation(summary = "소통 게시판 상세 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/{postId}")
    public ResponseEntity<CommonResponse<LoadPostDetailResponse>> loadPostDetail(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long postId,
            @CheckCurrentMember CurrentMember currentMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.loadPostDetail(currentMember.id(), postId, apartCode));
    }

    @Operation(summary = "소통 게시판 목록 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}")
    public ResponseEntity<CommonResponse<PageResponse<LoadPostListResponse>>> loadPostList(
        @NotNull @PathVariable String apartCode,
        @RequestParam(required = false, value = "apartAreaId") Long apartAreaId,
        @CheckCurrentMember CurrentMember currentMember,
        SearchPageRequest request
    ) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.loadPostList(currentMember.id(), apartCode, apartAreaId, command));
    }

    @Operation(summary = "소통 게시판 댓글 등록 API")
    @CheckApartType
    @PostMapping(value = "/{apartCode}/{postId}/comments", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WritePostCommentResponse>> writeComment(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long postId,
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestPart(value = "request") WritePostCommentRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        var command = postMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.writeComment(currentMember.id(), postId, apartCode, command, image));
    }

    @Operation(summary = "소통 게시판 댓글 수정 API")
    @CheckApartType
    @PatchMapping(value = "/{apartCode}/{postId}/comments/{commentId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WritePostCommentResponse>> modifyComment(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long postId,
            @NotNull @PathVariable Long commentId,
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestPart(value = "request") WritePostCommentRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        var command = postMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.modifyComment(currentMember.id(), postId, commentId, apartCode, command, image));
    }

    @Operation(summary = "소통 게시판 댓글 삭제 API")
    @CheckApartType
    @DeleteMapping(value = "/{apartCode}/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<DeletePostCommentResponse>> deleteComment(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long postId,
        @NotNull @PathVariable Long commentId,
        @CheckCurrentMember CurrentMember currentMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE, postFacade.deleteComment(currentMember.id(), postId, commentId, apartCode));
    }

    @Operation(summary = "소통 게시판 댓글 목록 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/{postId}/comments")
    public ResponseEntity<CommonResponse<PageResponse<LoadPostCommentListResponse>>> loadCommentList(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long postId,
        @CheckCurrentMember CurrentMember currentMember,
        CustomPageRequest request
    ) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.loadCommentList(currentMember.id(), apartCode, postId, command));
    }

    @Operation(summary = "소통 게시판 이모지 등록 API")
    @CheckApartType
    @PostMapping("/{apartCode}/{postId}/emoji")
    public ResponseEntity<CommonResponse<RegisterEmojiResponse>> registerEmoji(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long postId,
            @CheckCurrentMember CurrentMember currentMember,
            @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.registerEmoji(currentMember.id(), postId, apartCode, type));
    }

    @Operation(summary = "소통 게시판 이모지 삭제 API")
    @CheckApartType
    @DeleteMapping("/{apartCode}/{postId}/emoji")
    public ResponseEntity<CommonResponse<Void>> deleteEmoji(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long postId,
            @CheckCurrentMember CurrentMember currentMember,
            @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type
    ) {
        postFacade.deleteEmoji(currentMember.id(), postId, apartCode, type);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

    @Operation(summary = "아파트 평수 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/area")
    public ResponseEntity<CommonResponse<LoadApartAreaResponse>> loadApartArea(
            @NotNull @PathVariable String apartCode,
            @CheckCurrentMember CurrentMember currentMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.loadApartArea(apartCode));
    }

}
