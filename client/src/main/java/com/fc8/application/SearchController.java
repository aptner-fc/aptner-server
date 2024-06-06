package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentMember;
import com.fc8.facade.SearchFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.dto.record.CurrentMember;
import com.fc8.platform.dto.response.LoadUnifiedListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "통합 검색 관련 API")
@RestController
@RequestMapping(value = {"/v1/api/search"})
@RequiredArgsConstructor
public class SearchController {

    private final SearchFacade searchFacade;

    @Operation(summary = "통합 검색 API")
    @CheckApartType
    @GetMapping(value = "/{apartCode}")
    public ResponseEntity<CommonResponse<LoadUnifiedListResponse>> search(
        @NotNull @PathVariable String apartCode,
        @CheckCurrentMember CurrentMember currentMember,
        @RequestParam String keyword
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, searchFacade.search(currentMember.id(), apartCode, keyword));
    }

}
