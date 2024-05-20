package com.fc8.service.impl;

import com.fc8.platform.domain.entity.terms.Terms;
import com.fc8.platform.dto.record.UsedTermsInfo;
import com.fc8.platform.repository.TermsRepository;
import com.fc8.service.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;

    @Override
    public List<UsedTermsInfo> loadUsedTermsList() {
        List<Terms> termsList = termsRepository.getAllByIsUsed();
        return UsedTermsInfo.fromEntityList(termsList);
    }

}
