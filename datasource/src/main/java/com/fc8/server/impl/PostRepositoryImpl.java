package com.fc8.server.impl;

import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.QPost;
import com.fc8.platform.repository.PostRepository;
import com.fc8.server.PostJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PostJpaRepository postJpaRepository;

    QPost post = QPost.post;
    QMember member = QMember.member;
    QCategory category = QCategory.category;

    @Override
    public Post store(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Page<Post> getPostListByApartCode(Long memberId, String apartCode, Pageable pageable, String search) {
        List<Post> postList = jpaQueryFactory
                .selectFrom(post)
                .innerJoin(category).on(post.category.id.eq(category.id))
                .where(
                        // 1. 삭제된 포스트
                        isNotDeleted(),
                        // 2. 아파트 코드
                        eqApartCode(apartCode)
                        // 3. 회원 차단 TODO

                        // 4. 검색어
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = jpaQueryFactory
                .select(post.count())
                .from(post)
                .innerJoin(category).on(post.category.id.eq(category.id))
                .where(
                        // 1. 삭제된 포스트
                        isNotDeleted(),
                        // 2. 아파트 코드
                        eqApartCode(apartCode)
                        // 3. 회원 차단 TODO
                );

        return PageableExecutionUtils.getPage(postList, pageable, count::fetchOne);
    }

    private BooleanExpression eqApartCode(String apartCode) {
        return post.apart.code.eq(apartCode);
    }

    private BooleanExpression isNotDeleted() {
        return post.deletedAt.isNull();
    }

}
