package com.slop.slopbackend.utility;

import java.nio.charset.StandardCharsets;

public class SecurityUtil {
    private static final String SECRET = "SECRET";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final long EXPIRATION_TIME = 10L * 60 * 10000000;

    public static byte[] getTokenSecret() {
        // TODO: swap with an env
        return SECRET.getBytes(StandardCharsets.UTF_8);
    }

}
