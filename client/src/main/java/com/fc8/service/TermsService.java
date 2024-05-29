package com.fc8.service;

import com.fc8.platform.dto.record.UsedTermsInfo;

import java.util.List;

public interface TermsService {

    List<UsedTermsInfo> loadUsedTermsList();
}
