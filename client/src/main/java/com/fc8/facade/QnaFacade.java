package com.fc8.facade;

import com.fc8.platform.dto.command.CreateQnaCommand;
import com.fc8.platform.dto.response.CreateQnaResponse;
import com.fc8.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaFacade {

    private final QnaService qnaService;

    public CreateQnaResponse create(Long memberId, String apartCode, CreateQnaCommand command, MultipartFile image) {
        return new CreateQnaResponse(qnaService.create(memberId, apartCode, command, image));
    }

}
