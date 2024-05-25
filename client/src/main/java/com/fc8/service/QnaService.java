package com.fc8.service;

import com.fc8.platform.dto.command.CreateQnaCommand;
import com.fc8.platform.dto.record.QnaInfo;
import com.fc8.platform.dto.record.SearchPageCommand;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface QnaService {

    Long create(Long memberId, String apartCode, CreateQnaCommand command, MultipartFile image);

    Page<QnaInfo> loadQnaList(Long memberId, String apartCode, SearchPageCommand command);

    void deleteQna(Long memberId, Long qnaId, String apartCode);
}
