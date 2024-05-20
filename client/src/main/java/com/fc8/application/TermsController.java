package com.fc8.application;

import com.fc8.facade.TermsFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.dto.response.LoadTermsListResponse;
import com.fc8.platform.mapper.TermsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1/api/terms", "/api/terms"})
@RequiredArgsConstructor
public class TermsController {

    private final TermsMapper termsMapper;
    private final TermsFacade termsFacade;

    @GetMapping
    public ResponseEntity<CommonResponse<LoadTermsListResponse>> loadUsedTermsList() {
        return CommonResponse.success(SuccessCode.SUCCESS, termsFacade.loadUsedTermsList());
    }

}
