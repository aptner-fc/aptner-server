package com.fc8.service.impl;

import com.fc8.platform.domain.entity.disclosure.Disclosure;
import com.fc8.platform.domain.entity.notice.Notice;
import com.fc8.platform.domain.entity.post.Post;
import com.fc8.platform.domain.entity.qna.Qna;
import com.fc8.platform.dto.record.DisclosureInfo;
import com.fc8.platform.dto.record.NoticeInfo;
import com.fc8.platform.dto.record.PostInfo;
import com.fc8.platform.dto.record.QnaInfo;
import com.fc8.platform.repository.DisclosureRepository;
import com.fc8.platform.repository.NoticeRepository;
import com.fc8.platform.repository.PostRepository;
import com.fc8.platform.repository.QnaRepository;
import com.fc8.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final QnaRepository qnaRepository;
    private final PostRepository postRepository;
    private final DisclosureRepository disclosureRepository;
    private final NoticeRepository noticeRepository;


    @Override
    public List<PostInfo> searchPostList(Long memberId, String apartCode, String keyword, int pinnedPostCount) {
        if (isLimitReached(pinnedPostCount)) return null;

        List<Post> postList = postRepository.getPostListByKeyword(memberId, apartCode, keyword, pinnedPostCount);

        return postList.stream()
            .map(post -> PostInfo.fromEntity(post, post.getMember(), post.getCategory()))
            .toList();
    }

    @Override
    public List<QnaInfo> searchQnaList(Long memberId, String apartCode, String keyword, int pinnedQnaCount) {
        if (isLimitReached(pinnedQnaCount)) return null;

        List<Qna> qnaList = qnaRepository.getQnaListByKeyword(memberId, apartCode, keyword, pinnedQnaCount);

        return qnaList.stream()
            .map(qna -> QnaInfo.fromEntity(qna, qna.getMember(), qna.getCategory()))
            .toList();
    }

    @Override
    public List<DisclosureInfo> searchDisclosureList(String apartCode, String keyword, int pinnedDisclosureCount) {
        if (isLimitReached(pinnedDisclosureCount)) return null;

        List<Disclosure> disclosureList = disclosureRepository.getDisclosureListByKeyword(apartCode, keyword, pinnedDisclosureCount);

        return disclosureList.stream()
            .map(disclosure -> DisclosureInfo.fromEntity(disclosure, disclosure.getAdmin(), disclosure.getCategory()))
            .toList();
    }

    @Override
    public List<NoticeInfo> searchNoticeList(String apartCode, String keyword, int pinnedNoticeCount) {
        if (isLimitReached(pinnedNoticeCount)) return null;

        List<Notice> noticeList = noticeRepository.getNoticeListByKeyword(apartCode, keyword, pinnedNoticeCount);
        
        return noticeList.stream()
            .map(notice -> NoticeInfo.fromEntity(notice, notice.getAdmin(), notice.getCategory()))
            .toList();
    }

    private static boolean isLimitReached(int pinnedNoticeCount) {
        return pinnedNoticeCount >= 5;
    }
}
