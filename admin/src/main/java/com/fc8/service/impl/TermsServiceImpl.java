package com.fc8.service.impl;

import com.fc8.platform.dto.command.SavedTermsCommand;
import com.fc8.platform.repository.TermsRepository;
import com.fc8.service.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;

    @Override
    @Transactional
    public Long register(SavedTermsCommand command) {
        var terms = command.toEntity();
        var newTerms = termsRepository.store(terms);
        return newTerms.getId();
    }
}
