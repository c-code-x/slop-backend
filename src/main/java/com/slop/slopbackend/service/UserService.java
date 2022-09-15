package com.slop.slopbackend.service;


import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity getUserByEmailId(String emailId) {
        Optional<UserEntity> optionalUserEntity=userRepository.findByEmailId(emailId);
        if(optionalUserEntity.isEmpty())
            throw new ApiRuntimeException("User with email:\""+emailId+"\" does not exist",HttpStatus.NOT_FOUND);
        return optionalUserEntity.get();
    }

    public UserEntity saveUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        if(userRepository.existsByEmailId(userEntity.getEmailId()))
            throw new ApiRuntimeException("EmailId is already taken!",HttpStatus.ALREADY_REPORTED);
        if(userRepository.existsByRegistrationId(userEntity.getRegistrationId()))
            throw new ApiRuntimeException("Registration id is already taken!",HttpStatus.ALREADY_REPORTED);
        return userRepository.save(userEntity);
    }
}
