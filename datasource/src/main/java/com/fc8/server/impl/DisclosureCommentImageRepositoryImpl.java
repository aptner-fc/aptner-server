package com.fc8.server.impl;

import com.fc8.platform.domain.entity.disclosure.DisclosureCommentImage;
import com.fc8.platform.domain.entity.disclosure.QDisclosureComment;
import com.fc8.platform.domain.entity.disclosure.QDisclosureCommentImage;
import com.fc8.platform.repository.DisclosureCommentImageRepository;
import com.fc8.server.DisclosureCommentImageJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DisclosureCommentImageRepositoryImpl implements DisclosureCommentImageRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final DisclosureCommentImageJpaRepository disclosureCommentImageJpaRepository;

    QDisclosureCommentImage disclosureCommentImage = QDisclosureCommentImage.disclosureCommentImage;
    QDisclosureComment disclosureComment = QDisclosureComment.disclosureComment;

    @Override
    public DisclosureCommentImage store(DisclosureCommentImage disclosureCommentImage) {
        return disclosureCommentImageJpaRepository.save(disclosureCommentImage);
    }

    @Override
    public DisclosureCommentImage getImageByDisclosureCommentId(Long commentId) {
        return jpaQueryFactory
            .selectFrom(disclosureCommentImage)
            .innerJoin(disclosureCommentImage.disclosureComment, disclosureComment)
            .where(
                disclosureCommentImage.disclosureComment.id.eq(commentId),
                disclosureCommentImage.deletedAt.isNull()
            )
            .fetchOne();
    }
}
