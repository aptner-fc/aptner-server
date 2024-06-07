package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.DisclosureDetailInfo;
import com.fc8.platform.dto.record.DisclosureFileInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadDisclosureDetailResponse {

    DisclosureDetailInfo disclosureDetailInfo;
    List<DisclosureFileInfo> disclosureFileInfoList;

    public LoadDisclosureDetailResponse(DisclosureDetailInfo disclosureDetailInfo, List<DisclosureFileInfo> disclosureFileInfoList) {
        this.disclosureDetailInfo = disclosureDetailInfo;
        this.disclosureFileInfoList = disclosureFileInfoList;
    }
}
