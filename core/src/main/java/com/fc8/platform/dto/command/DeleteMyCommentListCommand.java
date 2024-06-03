package com.fc8.platform.dto.command;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DeleteMyCommentListCommand {

    private final List<Long> postCommentIds;
    private final List<Long> qnaCommentIds;

}
