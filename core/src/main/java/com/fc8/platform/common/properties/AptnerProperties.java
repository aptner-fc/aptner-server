package com.fc8.platform.common.properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AptnerProperties {

    /**
     * Date
     */
    public static final String DATE_FORMAT_YYYYMMDD = "yyyy/MM/dd";

    /**
     * Category
     */
    public static final String CATEGORY_POST = "post";
    public static final String CATEGORY_QNA = "qna";
    public static final String CATEGORY_MEMBER = "member";

    /**
     * File
     */
    public static final String FILE_DOT = ".";
    public static final int FILE_MAX_SIZE_MB = 10;
    public static final int FILE_MAX_SIZE_COUNT = 20;
}
