package com.fc8.platform.repository;


import com.fc8.platform.domain.entity.member.Member;
import com.fc8.platform.dto.record.LoadMyArticleInfo;
import com.fc8.platform.dto.record.LoadMyCommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository {

    Member store(Member member);

    Member getActiveMemberById(Long id);

    Member getByEmail(String email);

    boolean existActiveEmail(String email);

    boolean existNickname(String nickname);

    Page<LoadMyArticleInfo> getAllArticleByMemberAndApartCode(Member member, String apartCode, Pageable pageable);

    Page<LoadMyCommentInfo> getAllCommentByMemberAndApartCode(Member member, String apartCode, Pageable pageable);

}
