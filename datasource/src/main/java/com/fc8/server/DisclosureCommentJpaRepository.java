package com.fc8.server;

import com.fc8.platform.domain.entity.disclosure.DisclosureComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisclosureCommentJpaRepository extends JpaRepository<DisclosureComment, Long> {
}
