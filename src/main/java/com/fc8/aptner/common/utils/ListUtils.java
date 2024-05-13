package com.fc8.aptner.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListUtils {

    public static boolean isEqualSize(List<?> list1, List<?> list2) {
        return list1.size() == list2.size();
    }
}
