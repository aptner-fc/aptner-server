package com.fc8.server;

import com.fc8.platform.domain.entity.disclosure.DisclosureEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisclosureEmojiJpaRepository extends JpaRepository<DisclosureEmoji, Long> {
}
