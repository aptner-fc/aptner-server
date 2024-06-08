package com.fc8.server.impl;

import com.fc8.platform.domain.entity.pinned.PinnedPost;
import com.fc8.platform.domain.entity.pinned.PinnedPostFile;
import com.fc8.platform.domain.entity.pinned.QPinnedPost;
import com.fc8.platform.domain.entity.pinned.QPinnedPostFile;
import com.fc8.platform.repository.PinnedPostFileRepository;
import com.fc8.server.PinnedPostFileJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PinnedPostFileRepositoryImpl implements PinnedPostFileRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PinnedPostFileJpaRepository pinnedPostFileJpaRepository;

    QPinnedPost pinnedPost = QPinnedPost.pinnedPost;
    QPinnedPostFile pinnedPostFile = QPinnedPostFile.pinnedPostFile;

    @Override
    public List<PinnedPostFile> getAllByPinnedPost(PinnedPost pinnedPost) {
        return jpaQueryFactory
                .selectFrom(pinnedPostFile)
                .where(pinnedPostFile.pinnedPost.eq(pinnedPost))
                .fetch();
    }
}
