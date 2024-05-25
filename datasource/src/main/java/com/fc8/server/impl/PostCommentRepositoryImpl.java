package com.fc8.server.impl;

import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostComment;
import com.fc8.platform.domain.entity.post.QPostComment;
import com.fc8.platform.repository.PostCommentRepository;
import com.fc8.server.PostCommentJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCommentRepositoryImpl implements PostCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PostCommentJpaRepository postCommentJpaRepository;

    QPostComment postComment = QPostComment.postComment;

    @Override
    public PostComment getByIdAndPost(Long id, Post post) {
        return jpaQueryFactory
                .selectFrom(postComment)
                .where(
                        eqId(id),
                        eqPost(post),
                        isNotDeleted()
                )
                .fetchOne();
    }

    @Override
    public PostComment store(PostComment postComment) {
        return postCommentJpaRepository.save(postComment);
    }

    private BooleanExpression isNotDeleted() {
        return postComment.deletedAt.isNull();
    }

    private BooleanExpression eqPost(Post post) {
        return postComment.post.eq(post);
    }

    private BooleanExpression eqId(Long id) {
        return postComment.id.eq(id);
    }
}
