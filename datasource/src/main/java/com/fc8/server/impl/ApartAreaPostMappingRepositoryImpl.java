package com.fc8.server.impl;

import com.fc8.platform.domain.entity.apartment.QApartArea;
import com.fc8.platform.domain.entity.mapping.ApartAreaPostMapping;
import com.fc8.platform.domain.entity.mapping.QApartAreaPostMapping;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.QPost;
import com.fc8.platform.repository.ApartAreaPostMappingRepository;
import com.fc8.server.ApartAreaPostMappingJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ApartAreaPostMappingRepositoryImpl implements ApartAreaPostMappingRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ApartAreaPostMappingJpaRepository apartAreaPostMappingJpaRepository;

    QApartArea apartArea = QApartArea.apartArea;
    QPost post = QPost.post;
    QApartAreaPostMapping apartAreaPostMapping = QApartAreaPostMapping.apartAreaPostMapping;

    @Override
    public ApartAreaPostMapping store(ApartAreaPostMapping apartAreaPostMapping) {
        return apartAreaPostMappingJpaRepository.save(apartAreaPostMapping);
    }

    @Override
    public boolean existByPost(Post post) {
        return jpaQueryFactory
                .selectOne()
                .from(apartAreaPostMapping)
                .where(
                        apartAreaPostMapping.post.eq(post)
                )
                .fetchFirst() != null;
    }

    @Override
    public Optional<ApartAreaPostMapping> findByPost(Post post) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(apartAreaPostMapping)
                        .where(
                                apartAreaPostMapping.post.eq(post)
                        )
                        .fetchOne()
        );
    }

}
