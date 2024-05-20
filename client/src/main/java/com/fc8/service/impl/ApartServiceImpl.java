package com.fc8.service.impl;

import com.fc8.platform.repository.ApartRepository;
import com.fc8.service.ApartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApartServiceImpl implements ApartService {

    private final ApartRepository apartRepository;

    @Override
    @Transactional(readOnly = true)
    public String getContactByCode(String code) {
        return apartRepository.getContactByCode(code);
    }

}
