package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentMember;
import com.fc8.facade.DisclosureFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.domain.enums.EmojiType;
import com.fc8.platform.dto.record.CurrentMember;
import com.fc8.platform.dto.request.CustomPageRequest;
import com.fc8.platform.dto.request.SearchPageRequest;
import com.fc8.platform.dto.response.*;
import com.fc8.platform.mapper.PageMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "의무공개 관련 API")
@RestController
@RequestMapping(value = {"/v1/api/disclosures"})
@RequiredArgsConstructor
public class DisclosureController {

    private final DisclosureFacade disclosureFacade;
    private final PageMapper pageMapper;

    @Operation(summary = "의무공개 상세 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/{disclosureId}")
    public ResponseEntity<CommonResponse<LoadDisclosureDetailResponse>> loadDisclosureDetail(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long disclosureId,
        @CheckCurrentMember CurrentMember currentMember
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, disclosureFacade.loadDisclosureDetail(currentMember.id(), disclosureId, apartCode));
    }

    @Operation(summary = "의무공개 목록 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}")
    public ResponseEntity<CommonResponse<PageResponse<LoadDisclosureListResponse>>> loadDisclosureList(
        @NotNull @PathVariable String apartCode,
        @CheckCurrentMember CurrentMember currentMember,
        SearchPageRequest request
    ) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, disclosureFacade.loadDisclosureList(currentMember.id(), apartCode, command));
    }

    @Operation(summary = "의무공개 댓글 목록 조회 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}/{disclosureId}/comments")
    public ResponseEntity<CommonResponse<PageResponse<LoadDisclosureCommentListResponse>>> loadCommentList(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long disclosureId,
        @CheckCurrentMember CurrentMember currentMember,
        CustomPageRequest request
    ) {
        var command = pageMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, disclosureFacade.loadCommentList(currentMember.id(), apartCode, disclosureId, command));
    }

    @Operation(summary = "의무공개 이모지 등록 API")
    @CheckApartType
    @PostMapping("/{apartCode}/{disclosureId}/emoji")
    public ResponseEntity<CommonResponse<RegisterEmojiResponse>> registerEmoji(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long disclosureId,
        @CheckCurrentMember CurrentMember currentMember,
        @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, disclosureFacade.registerEmoji(currentMember.id(), disclosureId, apartCode, type));
    }

    @Operation(summary = "의무공개 이모지 삭제 API")
    @CheckApartType
    @DeleteMapping("/{apartCode}/{disclosureId}/emoji")
    public ResponseEntity<CommonResponse<Void>> deleteEmoji(
        @NotNull @PathVariable String apartCode,
        @NotNull @PathVariable Long disclosureId,
        @CheckCurrentMember CurrentMember currentMember,
        @NotNull(message = "이모지가 누락되었습니다.") @RequestParam EmojiType type
    ) {
        disclosureFacade.deleteEmoji(currentMember.id(), disclosureId, apartCode, type);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }


}
