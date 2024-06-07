package com.fc8.server.impl;

import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.disclosure.DisclosureFile;
import com.fc8.platform.domain.entity.disclosure.QDisclosureFile;
import com.fc8.platform.repository.DisclosureFileRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DisclosureFileRepositoryImpl implements DisclosureFileRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QDisclosureFile disclosureFile = QDisclosureFile.disclosureFile;

    @Override
    public List<DisclosureFile> getDisclosureFileListByDisclosure(Disclosure disclosure) {
        return jpaQueryFactory
            .selectFrom(disclosureFile)
            .where(disclosureFile.disclosure.eq(disclosure))
            .fetch();
    }
}

