package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.apartment.Apart;
import com.fc8.platform.domain.entity.member.Member;

import java.util.List;

public interface ApartRepository {

    Apart getByCode(String code);

    Apart getMainApartByMember(Member member);

    List<Apart> getNotMainApartListByMember(Member member);

    String getContactByCode(String code);
}
