package com.fc8.platform.dto.request;

import com.fc8.platform.domain.enums.SearchType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchPageRequest extends CustomPageRequest {

    private String search;
    private SearchType type;

}

