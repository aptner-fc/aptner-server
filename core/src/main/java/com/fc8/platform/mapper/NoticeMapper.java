package com.fc8.platform.mapper;

import com.fc8.platform.dto.command.WriteNoticeCommentCommand;
import com.fc8.platform.dto.request.WriteNoticeCommentRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface NoticeMapper {

    WriteNoticeCommentCommand of(WriteNoticeCommentRequest request);

}
