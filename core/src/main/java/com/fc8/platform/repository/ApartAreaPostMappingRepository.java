package com.fc8.platform.repository;


import com.fc8.platform.domain.entity.mapping.ApartAreaPostMapping;
import com.fc8.platform.domain.entity.post.Post;

import java.util.Optional;

public interface ApartAreaPostMappingRepository {

    ApartAreaPostMapping store(ApartAreaPostMapping apartAreaPostMapping);

    boolean existByPost(Post post);

    Optional<ApartAreaPostMapping> findByPost(Post post);
}
