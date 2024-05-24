package com.fc8.service;

import com.fc8.platform.dto.command.WritePostCommand;
import com.fc8.platform.dto.record.PostInfo;
import com.fc8.platform.dto.record.SearchPageCommand;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    Long write(Long memberId, String apartCode, WritePostCommand command, MultipartFile image);

    Page<PostInfo> loadPostList(Long memberId, String apartCode, SearchPageCommand command);
}
