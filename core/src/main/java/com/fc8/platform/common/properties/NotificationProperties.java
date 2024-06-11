package com.fc8.platform.common.properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class NotificationProperties {

    public static final String QNA_ANSWER_TITLE = "민원 처리가 완료되었습니다.";
    public static final String QNA_COMMENT_TITLE = "회원님이 작성한 게시글에 댓글이 등록되었습니다.";
    public static final String POST_COMMENT_TITLE = "회원님이 작성한 게시글에 댓글이 등록되었습니다.";

    public static String getQnaAnswerContent(Long qnaId) {
        return qnaId + "번 민원 처리가 완료되었습니다.";
    }

    public static String getQnaCommentContent(String nickName, String content) {
        return nickName + "님이 댓글을 남겼습니다 : " + content;
    }

    public static String getPostCommentContent(String nickName, String content) {
        return nickName + "님이 댓글을 남겼습니다 : " + content;
    }
}