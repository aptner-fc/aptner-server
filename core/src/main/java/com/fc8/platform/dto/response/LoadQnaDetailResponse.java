package com.fc8.platform.dto.response;

import com.fc8.platform.dto.record.QnaDetailInfo;
import lombok.Getter;

@Getter
public class LoadQnaDetailResponse {

    QnaDetailInfo qna;

    public LoadQnaDetailResponse(QnaDetailInfo qna) {
        this.qna = qna;
    }
}
