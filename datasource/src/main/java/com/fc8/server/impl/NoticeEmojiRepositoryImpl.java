package com.fc8.server.impl;

import com.fc8.platform.repository.NoticeEmojiRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeEmojiRepositoryImpl implements NoticeEmojiRepository {

    private final JPAQueryFactory jpaQueryFactory;



}
