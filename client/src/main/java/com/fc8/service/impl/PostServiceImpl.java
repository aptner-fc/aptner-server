package com.fc8.service.impl;

import com.fc8.platform.common.s3.S3Uploader;
import com.fc8.platform.dto.command.WritePostCommand;
import com.fc8.platform.repository.ApartRepository;
import com.fc8.platform.repository.CategoryRepository;
import com.fc8.platform.repository.MemberRepository;
import com.fc8.platform.repository.PostRepository;
import com.fc8.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ApartRepository apartRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public Long write(Long memberId, String apartCode, WritePostCommand command, MultipartFile image) {
        // 1. 카테고리 및 회원 검사 (상위 카테고리 : 중요 글, 하위 카테고리 : 본문)
        var category = categoryRepository.getChildCategoryByCode(command.getCategoryCode());
        var member = memberRepository.getActiveMemberById(memberId);

        // 2. 아파트 정보 조회
        var apart = apartRepository.getByCode(apartCode);

        // 3. 글 저장
        var post = command.toEntity(category, member, apart);
        var storedPost = postRepository.store(post);

        // 4. 썸네일 이미지 저장, s3Upload 수정 필요 TODO
        try {
            String url = s3Uploader.uploadFiles(image, "");
            storedPost.updateThumbnail(url);
        } catch (IOException e) {
            log.error("썸네일 업로드 실패");
        }

        // 5. 파일 저장 TODO

        return storedPost.getId();
    }
}
