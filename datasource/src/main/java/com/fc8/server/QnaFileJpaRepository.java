package com.fc8.server;

import com.fc8.platform.domain.entity.qna.QnaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaFileJpaRepository extends JpaRepository<QnaFile, Long> {
}
