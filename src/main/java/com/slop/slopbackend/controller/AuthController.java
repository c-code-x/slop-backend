package com.slop.slopbackend.controller;

import com.slop.slopbackend.dto.request.UserSigninReqDTO;
import com.slop.slopbackend.dto.request.UserSignupReqDTO;
import com.slop.slopbackend.dto.request.user.ChangePasswordReqDTO;
import com.slop.slopbackend.dto.response.user.UserResDTO;
import com.slop.slopbackend.dto.response.user.UserSignInResDTO;
import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.security.SecurityConfiguration;
import com.slop.slopbackend.service.EmailService;
import com.slop.slopbackend.service.OtpService;
import com.slop.slopbackend.service.UserDetailsServiceImpl;
import com.slop.slopbackend.service.UserService;
import com.slop.slopbackend.utility.JwtUtil;
import com.slop.slopbackend.utility.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final SecurityConfiguration securityConfiguration;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final OtpService otpService;
    @Autowired
    public AuthController(UserDetailsServiceImpl userDetailsService, UserService userService, SecurityConfiguration securityConfiguration, JwtUtil jwtUtil, EmailService emailService, OtpService otpService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.securityConfiguration = securityConfiguration;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.otpService = otpService;
    }
    @PostMapping("signin")
    public UserSignInResDTO signIn(@RequestBody @Valid UserSigninReqDTO userSigninReqDTO){
        try {
            securityConfiguration.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSigninReqDTO.getEmailId(),userSigninReqDTO.getPassword()));
        }catch (BadCredentialsException e){
            throw new ApiRuntimeException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        final UserEntity userEntity=userService.getUserByEmailId(userSigninReqDTO.getEmailId());

        String jwt= jwtUtil.generateToken(userDetailsService.loadUserByUsername(userEntity.getEmailId()));
        UserSignInResDTO userSignInResDTO= ModelMapperUtil.toObject(userEntity,UserSignInResDTO.class);
        userSignInResDTO.setJwt(jwt);
        return userSignInResDTO;
    }


    @PostMapping("signup")
    public UserResDTO signup(@RequestBody @Valid UserSignupReqDTO userSignupReqDTO){
        UserEntity userEntity= ModelMapperUtil.toUserEntity(userSignupReqDTO);
        return ModelMapperUtil.toUserResDTO(userService.saveUser(userEntity));
    }

    @PostMapping("forgot-password")
    public void forgotPassword(@RequestParam String emailId){
        otpService.sendOtp(emailId);
    }
    @PutMapping("update-password")
    public UserResDTO changePassword(@RequestBody @Valid ChangePasswordReqDTO changePasswordReqDTO, @RequestParam @Email String emailId){
        String otp=changePasswordReqDTO.getOtp();
        String password=changePasswordReqDTO.getPassword();
        UserEntity userEntity=userService.getUserByEmailId(emailId);
        if(!otpService.verifyOtp(emailId,otp))
            throw new ApiRuntimeException("Invalid OTP", HttpStatus.UNAUTHORIZED);
        return ModelMapperUtil.toUserResDTO(userService.updatePasswordById(password,userEntity.getId()));
    }
}
