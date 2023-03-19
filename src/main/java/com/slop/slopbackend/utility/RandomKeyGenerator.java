package com.slop.slopbackend.utility;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomKeyGenerator {

    private static final int KEY_LENGTH = 20; // Key length in bytes

    public static String generateRandomKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[KEY_LENGTH];
        random.nextBytes(keyBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
    }
}

