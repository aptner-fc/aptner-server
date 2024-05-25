package com.fc8.service;

import com.fc8.platform.dto.command.CreateQnaCommand;
import org.springframework.web.multipart.MultipartFile;

public interface QnaService {

    Long create(Long memberId, String apartCode, CreateQnaCommand command, MultipartFile image);

}
