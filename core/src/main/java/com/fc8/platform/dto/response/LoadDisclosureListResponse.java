package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.DisclosureInfo;
import com.fc8.platform.dto.record.PinnedPostSummary;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadDisclosureListResponse {

    private final List<DisclosureInfo> disclosureInfoList;

    private final List<PinnedPostSummary> pinnedDisclosureList;

    public LoadDisclosureListResponse(List<DisclosureInfo> disclosureInfoList, List<PinnedPostSummary> pinnedDisclosureList) {
        this.disclosureInfoList = disclosureInfoList;
        this.pinnedDisclosureList = pinnedDisclosureList;
    }
}
