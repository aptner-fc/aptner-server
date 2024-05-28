package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.PinnedQna;
import com.fc8.platform.dto.record.QnaInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadQnaListResponse {

    List<QnaInfo> qnas;
    List<PinnedQna> pinnedQnas;

    public LoadQnaListResponse(List<QnaInfo> qnas, List<PinnedQna> pinnedQnas) {
        this.qnas = qnas;
        this.pinnedQnas = pinnedQnas;
    }
}
