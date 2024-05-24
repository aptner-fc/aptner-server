package com.fc8.facade;

import com.fc8.platform.dto.command.WritePostCommand;
import com.fc8.platform.dto.response.WritePostResponse;
import com.fc8.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;

    public WritePostResponse write(Long memberId, String apartCode, WritePostCommand command, MultipartFile image) {
        return new WritePostResponse(postService.write(memberId, apartCode, command, image));
    }

}
