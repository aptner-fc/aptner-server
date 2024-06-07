package com.fc8.platform.common.properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class NotificationProperties {

    public static final String QNA_ANSWER_TITLE = "민원 처리가 완료되었습니다.";

    public static String getQnaAnswerContent(Long qnaId) {
        return qnaId + "번 민원 처리가 완료되었습니다.";
    }
}
