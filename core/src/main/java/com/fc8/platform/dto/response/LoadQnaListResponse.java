package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PinnedPostSummary;
import com.fc8.platform.dto.record.QnaInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadQnaListResponse {

    private final List<QnaInfo> qnas;
    private final List<PinnedPostSummary> pinnedQnas;

    public LoadQnaListResponse(List<QnaInfo> qnas, List<PinnedPostSummary> pinnedQnas) {
        this.qnas = qnas;
        this.pinnedQnas = pinnedQnas;
    }
}
