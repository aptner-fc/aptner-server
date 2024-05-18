package com.fc8.application;

import com.fc8.facade.TermsFacade;
import com.fc8.mapper.TermsMapper;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.dto.request.SavedTermsRequest;
import com.fc8.platform.dto.response.SavedTermsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1/api/terms", "/api/terms"})
@RequiredArgsConstructor
public class TermsController {

    private final TermsMapper termsMapper;
    private final TermsFacade termsFacade;

    @PostMapping
    public ResponseEntity<CommonResponse<SavedTermsResponse>> register(@Valid @RequestBody SavedTermsRequest request) {
        var command = termsMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, termsFacade.register(command));
    }

}
