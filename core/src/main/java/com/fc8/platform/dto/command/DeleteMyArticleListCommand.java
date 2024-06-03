package com.fc8.platform.dto.command;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DeleteMyArticleListCommand {

    private final List<Long> postIds;
    private final List<Long> qnaIds;

}
