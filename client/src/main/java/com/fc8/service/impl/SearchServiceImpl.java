package com.fc8.service.impl;

import com.fc8.platform.repository.DisclosureRepository;
import com.fc8.platform.repository.NoticeRepository;
import com.fc8.platform.repository.PostRepository;
import com.fc8.platform.repository.QnaRepository;
import com.fc8.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final QnaRepository qnaRepository;
    private final PostRepository postRepository;
    private final DisclosureRepository disclosureRepository;
    private final NoticeRepository noticeRepository;



}
