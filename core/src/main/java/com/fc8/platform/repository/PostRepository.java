package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.enums.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepository {

    Post store(Post post);

    Page<Post> getPostListByApartCode(Long memberId, String apartCode, Pageable pageable, String search, SearchType type, String categoryCode);

    Post getByIdAndApartCode(Long postId, String apartCode);

    Post getByIdAndMemberId(Long postId, Long memberId);

    Post getPostWithCategoryByIdAndApartCode(Long memberId, Long postId, String apartCode);

    boolean isWriter(Post post, Member member);

    List<Post> getAllByIdsAndMember(List<Long> postIds, Member member);

    List<Post> getPostListByKeyword(Long memberId, String apartCode, String keyword, int pinnedPostCount);
}
