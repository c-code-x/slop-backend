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
                .otp(String.valueOf(generateUniqueSixDigitNumber()))
                .username(username)
                .generatedTime(new Timestamp(System.currentTimeMillis()))
                .build();
    }
    private static int generateUniqueSixDigitNumber() {
        long timestamp = System.currentTimeMillis();
        int randomNumber = new Random().nextInt(900000) + 100000; // generate random number between 100000 and 999999
        int uniqueNumber = (int) (timestamp % 1000000); // get last 6 digits of timestamp
        uniqueNumber = (uniqueNumber * 1000000) + randomNumber; // add the random number
        return uniqueNumber%1000000;
    }
    public boolean isExpired() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return currentTime.getTime() - generatedTime.getTime() > otpExpiryTimeMillis;
    }
}
