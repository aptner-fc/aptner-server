package com.fc8.server;

import com.fc8.platform.domain.entity.post.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileJpaRepository extends JpaRepository<PostFile, Long> {

}
