package com.fc8.platform.mapper;

import com.fc8.platform.dto.command.CreateQnaCommand;
import com.fc8.platform.dto.record.SearchPageCommand;
import com.fc8.platform.dto.request.CreateQnaRequest;
import com.fc8.platform.dto.request.SearchPageRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface QnaMapper {

    CreateQnaCommand of(CreateQnaRequest request);

    SearchPageCommand of(SearchPageRequest request);

}
