package com.fc8.platform.common.properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AptnerProperties {

    public static final String DATE_FORMAT_YYYYMMDD = "yyyy/MM/dd";

    public static final String CATEGORY_POST = "post";
    public static final String CATEGORY_QNA = "qna";
    public static final String CATEGORY_MEMBER = "member";

    public static final String FILE_DOT = ".";
}
