package com.fc8.server.impl;

import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostFile;
import com.fc8.platform.domain.entity.post.QPostFile;
import com.fc8.platform.repository.PostFileRepository;
import com.fc8.server.PostFileJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostFileRepositoryImpl implements PostFileRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PostFileJpaRepository postFileJpaRepository;

    QPostFile postFile = QPostFile.postFile;

    @Override
    public PostFile store(PostFile postFile) {
        return postFileJpaRepository.save(postFile);
    }

    @Override
    public List<PostFile> storeAll(List<PostFile> postFile) {
        return postFileJpaRepository.saveAll(postFile);
    }

    @Override
    public List<PostFile> getPostFileListByPost(Post post) {
        return jpaQueryFactory
            .selectFrom(postFile)
            .where(postFile.post.eq(post))
            .fetch();
    }
}
