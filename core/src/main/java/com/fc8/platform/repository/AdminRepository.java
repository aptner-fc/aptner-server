package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.admin.Admin;
import com.fc8.platform.domain.enums.ApartType;

public interface AdminRepository {

    Admin getAdminWithApartByEmail(String email);

    Admin getMemberByEmail(String email);

    ApartType getApartTypeByAdmin(Admin admin);

    boolean existActiveEmail(String email);

    boolean existNickname(String nickname);

    Admin store(Admin admin);

}
