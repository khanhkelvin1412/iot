package com.hust.khanhkelvin.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public final class RandomUtil {
    private static final int DEF_COUNT = 20;
    private static final int SALT_COUNT = 10;
    private static final int HOUSE_COUNT = 10;

    private final static String ID_DATETIME_PATTERN = "yyyyMMddSSSHHssmm";

    private static final AtomicLong LAST_TIME_MS = new AtomicLong();

    private static final Random random = new Random();

    private RandomUtil() {
    }

    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    public static String generateSalt() {
        return RandomStringUtils.randomAlphanumeric(SALT_COUNT);
    }

    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    public static int generateRandom(int size) {
        return random.nextInt(size);
    }

    public static String genCodeHouse(){
        return RandomStringUtils.randomNumeric(HOUSE_COUNT);
    }
}
