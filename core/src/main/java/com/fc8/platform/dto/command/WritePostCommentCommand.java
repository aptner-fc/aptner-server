package com.fc8.platform.dto.command;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.pinned.PinnedPostComment;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostComment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WritePostCommentCommand {

    private final Long parentId;

    private final String content;

    public PostComment toEntity(Post post, Member member) {
        return PostComment.createComment(post, member, content);
    }

    public PostComment toEntity(Post post, PostComment parent, Member member) {
        return PostComment.createReply(post, parent, member, content);
    }

    public PinnedPostComment toEntity(PinnedPost pinnedPost, Member member) {
        return PinnedPostComment.createCommentByMember(pinnedPost, member, content);
    }

    public PinnedPostComment toEntity(PinnedPost pinnedPost, PinnedPostComment parent, Member member) {
        return PinnedPostComment.createReplyByMember(pinnedPost, parent, member, content);
    }
}
