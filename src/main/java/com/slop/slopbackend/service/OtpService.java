package com.slop.slopbackend.service;

import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.utility.OTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OtpService {
    private final UserService  userService;
    private final EmailService emailService;
    private final Map<String, OTP> otpMap;
    @Autowired
    public OtpService(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
        this.otpMap = new HashMap<>();
    }
    public void sendOtp(String emailId){
        UserEntity userEntity=userService.getUserByEmailId(emailId);
        OTP otp=OTP.geneateOTP(userEntity.getSecretKey());
        saveOTP(userEntity.getSecretKey(),otp);
        emailService.sendEmail(userEntity.getEmailId(), "SLOP FORGOT PASSWORD OTP", "Your OTP is for forgot password is "+otp.getOtp());
    }
    public boolean verifyOtp(String emailId, String otpString){
        UserEntity userEntity=userService.getUserByEmailId(emailId);
        OTP storedOTP= getOTP(userEntity.getSecretKey());
        if(storedOTP.isExpired())
            throw new ApiRuntimeException("OTP expired", HttpStatus.BAD_REQUEST);
        return storedOTP.getOtp().equals(otpString);
    }

    private void saveOTP(String userName, OTP otp) {
        otpMap.put(userName, otp);
    }
    private OTP getOTP(String userName) {
        if(!otpMap.containsKey(userName))
            throw new ApiRuntimeException("OTP not found", HttpStatus.BAD_REQUEST);
        return otpMap.get(userName);
    }
}
