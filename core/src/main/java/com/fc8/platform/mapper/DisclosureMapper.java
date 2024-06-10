package com.fc8.platform.mapper;

import com.fc8.platform.dto.command.WriteDisclosureCommentCommand;
import com.fc8.platform.dto.request.WriteDisclosureCommentRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface DisclosureMapper {

    WriteDisclosureCommentCommand of(WriteDisclosureCommentRequest request);

}
