package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.UsedTermsInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadTermsListResponse {

    private final List<UsedTermsInfo> termsInfoList;

    public LoadTermsListResponse(List<UsedTermsInfo> termsInfoList) {
        this.termsInfoList = termsInfoList;
    }
}
