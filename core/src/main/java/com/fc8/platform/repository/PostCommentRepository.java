package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCommentRepository {

    PostComment getByIdAndPost(Long id, Post post);

    PostComment store(PostComment postComment);

    PostComment getByIdAndPostIdAndMemberId(Long id, Long postId, Long memberId);

    boolean isWriter(PostComment comment, Member member);

    Page<PostComment> getCommentListByPost(Long memberId, Post post, Pageable pageable);
}
