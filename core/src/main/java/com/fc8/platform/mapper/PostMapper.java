package com.fc8.platform.mapper;

import com.fc8.platform.dto.command.WritePostCommand;
import com.fc8.platform.dto.request.WritePostRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PostMapper {

    WritePostCommand of(WritePostRequest request);
}
