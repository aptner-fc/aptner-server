package com.fc8.application;

import com.fc8.facade.TermsFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.dto.response.LoadTermsListResponse;
import com.fc8.platform.mapper.TermsMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "약관 관련 API", description = "약관 관련 API 모음 입니다.")
@RestController
@RequestMapping(value = {"/v1/api/terms"})
@RequiredArgsConstructor
public class TermsController {

    private final TermsMapper termsMapper;
    private final TermsFacade termsFacade;

    @Operation(summary = "약관 조회 API", description = "약관 조회 API 입니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<LoadTermsListResponse>> loadUsedTermsList() {
        return CommonResponse.success(SuccessCode.SUCCESS, termsFacade.loadUsedTermsList());
    }

}
