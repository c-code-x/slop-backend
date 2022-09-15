package com.slop.slopbackend.controller;

import com.slop.slopbackend.dto.request.UserSigninReqDTO;
import com.slop.slopbackend.dto.request.UserSignupReqDTO;
import com.slop.slopbackend.dto.response.user.UserResDTO;
import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.security.SecurityConfiguration;
import com.slop.slopbackend.service.UserDetailsServiceImpl;
import com.slop.slopbackend.service.UserService;
import com.slop.slopbackend.utility.JwtUtil;
import com.slop.slopbackend.utility.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final SecurityConfiguration securityConfiguration;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserDetailsServiceImpl userDetailsService, UserService userService, SecurityConfiguration securityConfiguration, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.securityConfiguration = securityConfiguration;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/signin")
    public String signIn(@RequestBody @Valid UserSigninReqDTO userSigninReqDTO){
        try {
            securityConfiguration.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSigninReqDTO.getEmailId(),userSigninReqDTO.getPassword()));
        }catch (BadCredentialsException e){
            throw new ApiRuntimeException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        final UserEntity userEntity=userService.getUserByEmailId(userSigninReqDTO.getEmailId());

        return jwtUtil.generateToken(userDetailsService.loadUserByUsername(userEntity.getEmailId()));
    }

    @PostMapping("/signup")
    public UserResDTO signup(@RequestBody @Valid UserSignupReqDTO userSignupReqDTO){
        UserEntity userEntity= ModelMapperUtil.toUserEntity(userSignupReqDTO);
        return ModelMapperUtil.toUserResDTO(userService.saveUser(userEntity));
    }
}
