package com.slop.slopbackend.utility;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Random;

@Data
@Builder
public class OTP {
    private String otp;
    private String username;
    private Timestamp generatedTime;
    @Builder.Default
    private int otpExpiryTimeMillis = 300000;

    public static OTP geneateOTP(String username) {
        return OTP.builder()
                .otp(RandomKeyGenerator.generateRandomString(6))
                .username(username)
                .generatedTime(new Timestamp(System.currentTimeMillis()))
                .build();
    }
    public boolean isExpired() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return currentTime.getTime() - generatedTime.getTime() > otpExpiryTimeMillis;
    }
}
