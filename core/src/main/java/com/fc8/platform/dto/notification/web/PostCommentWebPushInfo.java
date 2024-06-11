package com.fc8.platform.dto.notification.web;

import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.enums.MessageType;
import com.fc8.platform.domain.enums.NotificationType;
import com.fc8.platform.dto.record.PostSummary;
import lombok.Builder;

@Builder
public record PostCommentWebPushInfo(
    Long memberId,
    String title,
    String content,
    NotificationType notificationType,
    MessageType messageType,
    PostSummary post
) {
    public static PostCommentWebPushInfo fromPostEntity(String title, String content, Post post) {
        return PostCommentWebPushInfo.builder()
            .memberId(post.getMember().getId())
            .title(title)
            .content(content)
            .notificationType(NotificationType.POST)
            .messageType(MessageType.INFO)
            .post(PostSummary.fromEntity(post, post.getMember(), post.getCategory()))
            .build();
    }
}