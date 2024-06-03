package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostComment;
import com.fc8.platform.dto.record.PostCommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostCommentRepository {

    PostComment getByIdAndPost(Long id, Post post);

    PostComment store(PostComment postComment);

    PostComment getByIdAndPostIdAndMemberId(Long id, Long postId, Long memberId);

    boolean isWriter(PostComment comment, Member member);

    List<PostComment> getAllByIdsAndMember(List<Long> postCommentIds, Member member);

    Page<PostCommentInfo> getCommentListByPost(Long postId, Pageable pageable);
}
