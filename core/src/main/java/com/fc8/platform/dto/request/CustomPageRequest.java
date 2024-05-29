package com.fc8.platform.dto.request;

import com.fc8.platform.domain.enums.SortType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomPageRequest {

    private int page;

    private int size;

    private SortType sort;

    protected CustomPageRequest() {
        this.page = 1;
        this.size = 10;
        this.sort = SortType.LATEST;
    }

}

