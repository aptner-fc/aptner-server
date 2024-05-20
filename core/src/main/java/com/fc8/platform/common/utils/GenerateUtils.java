package com.fc8.platform.common.utils;

import com.fc8.platform.common.properties.RedisProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateUtils {

    private static final Random RANDOM = new Random();

    public static String generateVerificationCode() {
        return IntStream.range(0, RedisProperties.VERIFICATION_CODE_LENGTH)
                .mapToObj(i -> String.valueOf(RANDOM.nextInt(10)))
                .collect(Collectors.joining());
    }
}
