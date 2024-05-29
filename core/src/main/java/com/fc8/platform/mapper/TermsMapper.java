package com.fc8.platform.mapper;

import com.fc8.platform.dto.command.SavedTermsCommand;
import com.fc8.platform.dto.request.SavedTermsRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TermsMapper {

    @Mapping(target = "isUsed", source = "used")
    @Mapping(target = "isRequired", source = "required")
    SavedTermsCommand of(SavedTermsRequest request);
}
