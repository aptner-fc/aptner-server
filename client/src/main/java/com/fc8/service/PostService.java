package com.fc8.service;

import com.fc8.platform.dto.command.WritePostCommand;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    Long write(Long memberId, String apartCode, WritePostCommand command, MultipartFile image);
}
