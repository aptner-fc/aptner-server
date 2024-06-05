package com.fc8.server.impl;

import com.fc8.platform.common.exception.InvalidParamException;
import com.fc8.platform.common.exception.code.ErrorCode;
import com.fc8.platform.domain.entity.apartment.QApart;
import com.fc8.platform.domain.entity.category.QCategory;
import com.fc8.platform.domain.entity.mapping.QApartMemberMapping;
import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.domain.entity.member.QMember;
import com.fc8.platform.domain.entity.member.QMemberBlock;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.post.PostComment;
import com.fc8.platform.domain.entity.post.QPost;
import com.fc8.platform.domain.entity.post.QPostComment;
import com.fc8.platform.domain.entity.qna.QQna;
import com.fc8.platform.domain.entity.qna.QQnaComment;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.domain.entity.qna.QnaComment;
import com.fc8.platform.dto.record.LoadMyArticleInfo;
import com.fc8.platform.dto.record.LoadMyCommentInfo;
import com.fc8.platform.dto.record.MemberSummary;
import com.fc8.platform.repository.MemberRepository;
import com.fc8.server.MemberJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberJpaRepository memberJpaRepository;

    QMember member = QMember.member;
    QMemberBlock memberBlock = QMemberBlock.memberBlock;
    QApart apart = QApart.apart;
    QApartMemberMapping apartMemberMapping = QApartMemberMapping.apartMemberMapping;
    QPost post = QPost.post;
    QQnaComment qnaComment = QQnaComment.qnaComment;
    QPostComment postComment = QPostComment.postComment;
    QQna qna = QQna.qna;
    QCategory category = QCategory.category;

    @Override
    public Member getByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Override
    public boolean existActiveEmail(String email) {
        return jpaQueryFactory
                .selectOne()
                .from(member)
                .where(
                        eqEmail(email),
                        isNotWithdrawal(member)
                )
                .fetchFirst() != null;
    }

    @Override
    public boolean existNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existPhone(String phone) {
        return jpaQueryFactory
                .selectOne()
                .from(member)
                .where(
                        eqPhone(member, phone),
                        isNotWithdrawal(member)
                )
                .fetchFirst() != null;
    }

    @Override
    public Page<LoadMyArticleInfo> getAllArticleByMemberAndApartCode(Member activeMember, String apartCode, Pageable pageable) {
        List<Post> myPostList = jpaQueryFactory
                .selectFrom(post)
                .innerJoin(category).on(post.category.id.eq(category.id))
                .innerJoin(member).on(post.member.id.eq(member.id))
                .where(
                        isNotDeleted(post),
                        eqApartCode(post, apartCode),
                        eqMember(post.member, activeMember)
                )
                .fetch();

        List<Qna> myQnaList = jpaQueryFactory
                .selectFrom(qna)
                .innerJoin(category).on(qna.category.id.eq(category.id))
                .innerJoin(member).on(qna.member.id.eq(member.id))
                .where(
                        isNotDeleted(qna),
                        eqApartCode(qna, apartCode),
                        eqMember(qna.member, activeMember)
                )
                .fetch();

        Long postCount = jpaQueryFactory
                .select(post.count())
                .from(post)
                .innerJoin(category).on(post.category.id.eq(category.id))
                .innerJoin(member).on(post.member.id.eq(member.id))
                .where(
                        isNotDeleted(post),
                        eqApartCode(post, apartCode),
                        eqMember(post.member, activeMember)
                )
                .fetchOne();

        Long qnaCount = jpaQueryFactory
                .select(qna.count())
                .from(qna)
                .innerJoin(category).on(qna.category.id.eq(category.id))
                .innerJoin(member).on(qna.member.id.eq(member.id))
                .where(
                        isNotDeleted(qna),
                        eqApartCode(qna, apartCode),
                        eqMember(qna.member, activeMember)
                )
                .fetchOne();

        Stream<LoadMyArticleInfo> postStream = myPostList.stream()
                .map(p -> LoadMyArticleInfo.fromPost(p, p.getCategory()));

        Stream<LoadMyArticleInfo> qnaStream = myQnaList.stream()
                .map(q -> LoadMyArticleInfo.fromQna(q, q.getCategory()));

        List<LoadMyArticleInfo> combined = Stream.concat(postStream, qnaStream)
                .sorted(Comparator.comparing(LoadMyArticleInfo::createdAt).reversed())
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), combined.size());

        List<LoadMyArticleInfo> myArticleList = combined.subList(start, end);
        long myArticleCount = Optional.ofNullable(postCount).orElse(0L) + Optional.ofNullable(qnaCount).orElse(0L);
        return new PageImpl<>(myArticleList, pageable, myArticleCount);
    }

    @Override
    public Page<LoadMyCommentInfo> getAllCommentByMemberAndApartCode(Member activeMember, String apartCode, Pageable pageable) {
        List<PostComment> myPostCommentList = jpaQueryFactory
                .selectFrom(postComment)
                .innerJoin(post).on(postComment.post.id.eq(post.id))
                .innerJoin(category).on(post.category.id.eq(category.id))
                .innerJoin(member).on(postComment.member.id.eq(member.id))
                .where(
                        isNotDeleted(postComment),
                        eqApartCode(post, apartCode),
                        eqMember(postComment.member, activeMember)
                )
                .fetch();

        List<QnaComment> myQnaCommentList = jpaQueryFactory
                .selectFrom(qnaComment)
                .innerJoin(qna).on(qnaComment.qna.id.eq(qna.id))
                .innerJoin(category).on(qna.category.id.eq(category.id))
                .innerJoin(member).on(qnaComment.member.id.eq(member.id))
                .where(
                        isNotDeleted(qnaComment),
                        eqApartCode(qna, apartCode),
                        eqMember(qnaComment.member, activeMember)
                )
                .fetch();

        Long postCount = jpaQueryFactory
                .select(postComment.count())
                .from(postComment)
                .innerJoin(post).on(postComment.post.id.eq(post.id))
                .innerJoin(member).on(postComment.member.id.eq(member.id))
                .where(
                        isNotDeleted(postComment),
                        eqApartCode(post, apartCode),
                        eqMember(postComment.member, activeMember)
                )
                .fetchOne();

        Long qnaCount = jpaQueryFactory
                .select(qnaComment.count())
                .from(qnaComment)
                .innerJoin(qna).on(qnaComment.qna.id.eq(qna.id))
                .innerJoin(member).on(qnaComment.member.id.eq(member.id))
                .where(
                        isNotDeleted(qnaComment),
                        eqApartCode(qna, apartCode),
                        eqMember(qnaComment.member, activeMember)
                )
                .fetchOne();

        Stream<LoadMyCommentInfo> postStream = myPostCommentList.stream()
                .map(p -> LoadMyCommentInfo.fromPost(p.getPost(), p, p.getPost().getCategory()));

        Stream<LoadMyCommentInfo> qnaStream = myQnaCommentList.stream()
                .map(q -> LoadMyCommentInfo.fromQna(q.getQna(), q, q.getQna().getCategory()));

        List<LoadMyCommentInfo> combined = Stream.concat(postStream, qnaStream)
                .sorted(Comparator.comparing(LoadMyCommentInfo::createdAt).reversed())
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), combined.size());

        List<LoadMyCommentInfo> myCommentList = combined.subList(start, end);
        long myCommentCount = Optional.ofNullable(postCount).orElse(0L) + Optional.ofNullable(qnaCount).orElse(0L);
        return new PageImpl<>(myCommentList, pageable, myCommentCount);
    }

    @Override
    public Member getByApartCodeAndNameAndPhone(String apartCode, String name, String phone) {
        Member activeMember = jpaQueryFactory
                .selectFrom(member)
                .innerJoin(apartMemberMapping).on(member.eq(apartMemberMapping.member))
                .innerJoin(apart).on(apartMemberMapping.apart.eq(apart))
                .where(
                        isNotWithdrawal(member),
                        member.name.eq(name),
                        member.phone.eq(phone),
                        eqApartCode(apart, apartCode)
                )
                .fetchOne();

        return Optional.ofNullable(activeMember)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Override
    public Member getByApartCodeAndEmail(String apartCode, String email) {
        Member activeMember = jpaQueryFactory
                .selectFrom(member)
                .innerJoin(apartMemberMapping).on(member.eq(apartMemberMapping.member))
                .innerJoin(apart).on(apartMemberMapping.apart.eq(apart))
                .where(
                        isNotWithdrawal(member),
                        member.email.eq(email),
                        eqApartCode(apart, apartCode)
                )
                .fetchOne();

        return Optional.ofNullable(activeMember)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Override
    public Page<MemberSummary> getAllBlockedMemberByMemberAndApartCode(Member activeMember, String apartCode, Pageable pageable) {
        List<Member> memberList = jpaQueryFactory
                .selectFrom(member)
                .innerJoin(memberBlock).on(member.id.eq(memberBlock.blocked.id))
                .where(
                        isNotWithdrawal(memberBlock.member),
                        isNotWithdrawal(memberBlock.blocked),
                        eqMember(memberBlock.member, activeMember)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(memberBlock.blockedAt.desc())
                .fetch();

        JPAQuery<Long> count = jpaQueryFactory
                .select(member.count())
                .from(member)
                .innerJoin(memberBlock).on(member.id.eq(memberBlock.blocked.id))
                .where(
                        isNotWithdrawal(memberBlock.member),
                        isNotWithdrawal(memberBlock.blocked),
                        eqMember(memberBlock.member, activeMember)
                );

        return PageableExecutionUtils.getPage(
                memberList.stream()
                        .map(MemberSummary::fromEntity)
                        .toList(), pageable, count::fetchOne);
    }

    @Override
    public Member store(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Member getActiveMemberById(Long id) {
        Member activeMember = jpaQueryFactory
                .selectFrom(member)
                .where(
                        eqId(id),
                        isNotWithdrawal(member)
                )
                .fetchOne();

        return Optional.ofNullable(activeMember)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Override
    public Member getByIdAndApartCode(Long id, String apartCode) {
        Member activeMember = jpaQueryFactory
                .selectFrom(member)
                .innerJoin(apartMemberMapping).on(member.eq(apartMemberMapping.member))
                .innerJoin(apart).on(apartMemberMapping.apart.eq(apart))
                .where(
                        eqId(id),
                        isNotWithdrawal(member),
                        eqApartCode(apart, apartCode)
                )
                .fetchOne();

        return Optional.ofNullable(activeMember)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private BooleanExpression eqApartCode(QApart apart, String apartCode) {
        return apart.code.eq(apartCode);
    }

    @Override
    public Member getActiveMemberByEmail(String email) {
        Member activeMember = jpaQueryFactory
                .selectFrom(member)
                .where(
                        eqEmail(email),
                        isNotWithdrawal(member)
                )
                .fetchOne();

        return Optional.ofNullable(activeMember)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private BooleanExpression eqMember(QMember member, Member activeMember) {
        return member.id.eq(activeMember.getId());
    }

    private BooleanExpression eqApartCode(QPost post, String apartCode) {
        return post.apart.code.eq(apartCode);
    }

    private BooleanExpression eqApartCode(QQna qna, String apartCode) {
        return qna.apart.code.eq(apartCode);
    }

    private BooleanExpression isNotDeleted(QPost post) {
        return post.deletedAt.isNull();
    }

    private BooleanExpression isNotDeleted(QPostComment postComment) {
        return postComment.deletedAt.isNull();
    }

    private BooleanExpression isNotDeleted(QQnaComment qnaComment) {
        return qnaComment.deletedAt.isNull();
    }

    private BooleanExpression isNotDeleted(QQna qna) {
        return qna.deletedAt.isNull();
    }


    private BooleanExpression isNotWithdrawal(QMember member) {
        return member.deletedAt.isNull();
    }

    private BooleanExpression eqEmail(String email) {
        return member.email.eq(email);
    }

    private BooleanExpression eqPhone(QMember member, String phone) {
        return member.phone.eq(phone);
    }

    private BooleanExpression eqId(Long id) {
        return member.id.eq(id);
    }

}
