package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.QnaDetailInfo;
import com.fc8.platform.dto.record.QnaFileInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadQnaDetailResponse {

    private final QnaDetailInfo qna;
    private final List<QnaFileInfo> qnaFileInfoList;

    public LoadQnaDetailResponse(QnaDetailInfo qna, List<QnaFileInfo> qnaFileInfoList) {
        this.qna = qna;
        this.qnaFileInfoList = qnaFileInfoList;
    }
}
