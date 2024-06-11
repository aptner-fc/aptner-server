package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PinnedPostInfo;
import com.fc8.platform.dto.record.QnaInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadQnaListResponse {

    private final List<QnaInfo> qnas;
    private final List<PinnedPostInfo> pinnedQnas;

    public LoadQnaListResponse(List<QnaInfo> qnas, List<PinnedPostInfo> pinnedQnas) {
        this.qnas = qnas;
        this.pinnedQnas = pinnedQnas;
    }
}
