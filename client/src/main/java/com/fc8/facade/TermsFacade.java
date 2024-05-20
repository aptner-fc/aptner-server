package com.fc8.facade;

import com.fc8.platform.dto.response.LoadTermsListResponse;
import com.fc8.service.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermsFacade {

    private final TermsService termsService;

    public LoadTermsListResponse loadUsedTermsList() {
        return new LoadTermsListResponse(termsService.loadUsedTermsList());
    }
}
