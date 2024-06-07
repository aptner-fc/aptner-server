package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.DisclosureInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadDisclosureListResponse {

    List<DisclosureInfo> disclosureInfoList;

    public LoadDisclosureListResponse(List<DisclosureInfo> disclosureInfoList) {
        this.disclosureInfoList = disclosureInfoList;
    }
}
