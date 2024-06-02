package com.fc8.platform.repository;

import com.fc8.platform.domain.entity.post.PostFile;

import java.util.List;

public interface PostFileRepository {

    PostFile store(PostFile postFile);

    List<PostFile> storeAll(List<PostFile> postFile);

}
