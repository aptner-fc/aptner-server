package com.fc8.platform.repository;


import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.dto.record.LoadMyArticleInfo;
import com.fc8.platform.dto.record.LoadMyCommentInfo;
import com.fc8.platform.dto.record.MemberSummary;
import com.fc8.platform.dto.record.NotificationInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository {

    Member store(Member member);

    Member getActiveMemberById(Long id);

    Member getByIdAndApartCode(Long id, String apartCode);

    Member getActiveMemberByEmail(String email);

    Member getByEmail(String email);

    boolean existActiveEmail(String email);

    boolean existNickname(String nickname);

    boolean existPhone(String phone);

    Page<LoadMyArticleInfo> getAllArticleByMemberAndApartCode(Member member, String apartCode, Pageable pageable);

    Page<LoadMyCommentInfo> getAllCommentByMemberAndApartCode(Member member, String apartCode, Pageable pageable);

    Member getByApartCodeAndNameAndPhone(String apartCode, String name, String phone);

    Member getByApartCodeAndEmail(String apartCode, String email);

    Page<MemberSummary> getAllBlockedMemberByMemberAndApartCode(Member member, String apartCode, Pageable pageable);

    Page<NotificationInfo> getAllNotificationByMember(Member member, Pageable pageable);

}
