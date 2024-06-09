package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.DisclosureInfo;
import com.fc8.platform.dto.record.PinnedPostSummary;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadDisclosureListResponse {

    List<DisclosureInfo> disclosureInfoList;

    private final List<PinnedPostSummary> pinnedPosts;

    public LoadDisclosureListResponse(List<DisclosureInfo> disclosureInfoList, List<PinnedPostSummary> pinnedPosts) {
        this.disclosureInfoList = disclosureInfoList;
        this.pinnedPosts = pinnedPosts;
    }
}
