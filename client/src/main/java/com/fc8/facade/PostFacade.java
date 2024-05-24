package com.fc8.facade;

import com.fc8.platform.dto.command.WritePostCommand;
import com.fc8.platform.dto.record.PostInfo;
import com.fc8.platform.dto.record.SearchPageCommand;
import com.fc8.platform.dto.response.LoadPostListResponse;
import com.fc8.platform.dto.response.PageResponse;
import com.fc8.platform.dto.response.WritePostResponse;
import com.fc8.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
//    private final PinnedPostService pinnedPostService;

    public WritePostResponse write(Long memberId, String apartCode, WritePostCommand command, MultipartFile image) {
        return new WritePostResponse(postService.write(memberId, apartCode, command, image));
    }

    @Transactional(readOnly = true)
    public PageResponse<LoadPostListResponse> loadPostList(Long memberId, String apartCode, SearchPageCommand command) {
        final Page<PostInfo> posts = postService.loadPostList(memberId, apartCode, command);
        // 상단 고정 게시물
        return new PageResponse<>(posts, new LoadPostListResponse(posts.getContent(), null));
    }

}
