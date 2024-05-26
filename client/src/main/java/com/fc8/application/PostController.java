package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentMember;
import com.fc8.facade.PostFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.CurrentMember;
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

@Tag(name = "소통 게시판 관련 API", description = "소통 게시판 관련 API 모음 입니다.")
@RestController
@RequestMapping(value = {"/v1/api/posts"})
@RequiredArgsConstructor
public class PostController {

    private final PostMapper postMapper;
    private final PageMapper pageMapper;
    private final PostFacade postFacade;

    @Operation(summary = "소통 게시판 글 작성 API", description = "소통 게시판 글 작성 API 입니다.")
    @CheckApartType
    @PostMapping(value = "/{apartCode}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WritePostResponse>> writePost(
            @NotNull @PathVariable String apartCode,
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestPart(value = "request") WritePostRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        var command = postMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, postFacade.writePost(currentMember.id(), apartCode, command, image));
    }

    @Operation(summary = "소통 게시판 목록 조회", description = "소통 게시판의 목록을 조회합니다.")
    @CheckApartType
    @GetMapping(value = "/{apartCode}")
    public ResponseEntity<CommonResponse<PageResponse<LoadPostListResponse>>> loadPostList(
            @NotNull @PathVariable String apartCode,
            @CheckCurrentMember CurrentMember currentMember,
            SearchPageRequest request) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.loadPostList(currentMember.id(), apartCode, command));
    }

    @Operation(summary = "소통 게시판 상세 조회", description = "소통 게시판의 상세 정보를 조회합니다.")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/{postId}")
    public ResponseEntity<CommonResponse<LoadPostDetailResponse>> loadPostDetail(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long postId,
            @CheckCurrentMember CurrentMember currentMember) {
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.loadPostDetail(currentMember.id(), postId, apartCode));
    }

    @Operation(summary = "소통 게시판 글 삭제 API", description = "소통 게시판 글 삭제 API 입니다.")
    @CheckApartType
    @DeleteMapping(value = "/{apartCode}/{postId}")
    public ResponseEntity<CommonResponse<DeletePostResponse>> deletePost(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long postId,
            @CheckCurrentMember CurrentMember currentMember) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE, postFacade.deletePost(currentMember.id(), postId, apartCode));
    }

    @Operation(summary = "소통 게시판 댓글 작성", description = "소통 게시판의 댓글을 작성합니다.")
    @CheckApartType
    @PostMapping(value = "/{apartCode}/{postId}/comments", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WritePostCommentResponse>> writeComment(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long postId,
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestPart(value = "request") WritePostCommentRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        var command = postMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.writeComment(currentMember.id(), postId, apartCode, command, image));
    }

    @Operation(summary = "소통 게시판 이모지 등록", description = "소통 게시판 이모지를 등록합니다.")
    @CheckApartType
    @PostMapping("/{apartCode}/{postId}/emoji")
    public ResponseEntity<CommonResponse<RegisterEmojiResponse>> registerEmoji(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long postId,
            @CheckCurrentMember CurrentMember currentMember,
            @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type) {
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.registerEmoji(currentMember.id(), postId, apartCode, type));
    }

    @Operation(summary = "소통 게시판 이모지 삭제", description = "소통 게시판 이모지를 삭제합니다.")
    @CheckApartType
    @DeleteMapping("/{apartCode}/{postId}/emoji")
    public ResponseEntity<CommonResponse<Void>> deleteEmoji(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long postId,
            @CheckCurrentMember CurrentMember currentMember,
            @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type) {
        postFacade.deleteEmoji(currentMember.id(), postId, apartCode, type);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

}
