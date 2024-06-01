package com.fc8.platform.mapper;

import com.fc8.platform.dto.command.WriteQnaCommand;
import com.fc8.platform.dto.command.WriteQnaCommentCommand;
import com.fc8.platform.dto.request.WriteQnaRequest;
import com.fc8.platform.dto.request.WriteQnaCommentRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface QnaMapper {

    @Mapping(target = "isPrivate", source = "private")
    WriteQnaCommand of(WriteQnaRequest request);

    WriteQnaCommentCommand of(WriteQnaCommentRequest request);

}
