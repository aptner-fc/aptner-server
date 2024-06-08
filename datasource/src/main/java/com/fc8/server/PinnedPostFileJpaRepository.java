package com.fc8.server;

import com.fc8.platform.domain.entity.pinned.PinnedPostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinnedPostFileJpaRepository extends JpaRepository<PinnedPostFile, Long> {
}
