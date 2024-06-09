package com.fc8.server.impl;

import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.pinned.PinnedPostCommentImage;
import com.fc8.platform.domain.entity.pinned.QPinnedPostComment;
import com.fc8.platform.domain.entity.pinned.QPinnedPostCommentImage;
import com.fc8.platform.repository.PinnedPostCommentImageRepository;
import com.fc8.server.PinnedPostCommentImageJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PinnedPostCommentImageRepositoryImpl implements PinnedPostCommentImageRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PinnedPostCommentImageJpaRepository pinnedPostCommentImageJpaRepository;

    QPinnedPostCommentImage pinnedPostCommentImage = QPinnedPostCommentImage.pinnedPostCommentImage;
    QPinnedPostComment pinnedPostComment = QPinnedPostComment.pinnedPostComment;
    QMember member = QMember.member;


    @Override
    public PinnedPostCommentImage store(PinnedPostCommentImage pinnedPostCommentImage) {
        return pinnedPostCommentImageJpaRepository.save(pinnedPostCommentImage);
    }

    @Override
    public PinnedPostCommentImage getByPinnedPostCommentId(Long commentId) {
        return jpaQueryFactory
                .selectFrom(pinnedPostCommentImage)
                .innerJoin(pinnedPostComment, pinnedPostCommentImage.pinnedPostComment)
                .where(
                        pinnedPostCommentImage.pinnedPostComment.id.eq(commentId),
                        pinnedPostCommentImage.deletedAt.isNull()
                )
                .fetchOne();

    }

}
