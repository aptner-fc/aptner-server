package com.fc8.platform.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageResponse<T> {

    private final long totalCount;
    private final int totalPages;
    private final int currentPage;
    private final T result;

    public PageResponse(Page<?> pageList, T response) {
        this.totalCount = pageList.getTotalElements();
        this.totalPages = pageList.getTotalPages();
        this.currentPage = pageList.getNumber() + 1;
        this.result = response;
    }
}
