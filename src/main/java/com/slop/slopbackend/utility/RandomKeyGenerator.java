package com.slop.slopbackend.utility;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomKeyGenerator {

    private static final int KEY_LENGTH = 20; // Key length in bytes
    private static final SecureRandom random = new SecureRandom();
    public static String generateRandomKey() {
        byte[] keyBytes = new byte[KEY_LENGTH];
        random.nextBytes(keyBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
    }
    public static String generateRandomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}

