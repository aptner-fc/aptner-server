package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentMember;
import com.fc8.facade.QnaFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.dto.record.CurrentMember;
import com.fc8.platform.dto.request.CreateQnaRequest;
import com.fc8.platform.dto.request.SearchPageRequest;
import com.fc8.platform.dto.response.CreateQnaResponse;
import com.fc8.platform.dto.response.LoadQnaListResponse;
import com.fc8.platform.dto.response.PageResponse;
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

@Tag(name = "민원게시판 관련 API")
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
    public ResponseEntity<CommonResponse<CreateQnaResponse>> createQna(
        @NotNull @PathVariable String apartCode,
        @CheckCurrentMember CurrentMember currentMember,
        @Valid @RequestPart(value = "request") CreateQnaRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        var command = qnaMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, qnaFacade.create(currentMember.id(), apartCode, command, image));
    }

    // 민원 수정
    @Operation(summary = "민원 삭제 API")
    @CheckApartType
    @DeleteMapping("/{apartCode}/{qnaId}")
    public ResponseEntity<CommonResponse<CreateQnaResponse>> deleteQna(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long qnaId,
        @CheckCurrentMember CurrentMember currentMember
    ) {
        qnaFacade.deleteQna(currentMember.id(), qnaId, apartCode);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }
    // 민원 상세 조회

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

    // 민원 댓글 등록
    // 민원 댓글 수정
    // 민원 댓글 삭제
    // 민원 댓글 목록 조회

}
