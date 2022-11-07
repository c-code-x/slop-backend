package com.slop.slopbackend.service;


import com.slop.slopbackend.dto.request.user.UpdateEmailIdReqDTO;
import com.slop.slopbackend.dto.request.user.UpdatePasswordReqDTO;
import com.slop.slopbackend.dto.request.user.UpdateUserReqDTO;
import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

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
        if(userRepository.existsByPhoneNumber(userEntity.getPhoneNumber()))
            throw new ApiRuntimeException("Phone number is already taken!",HttpStatus.ALREADY_REPORTED);
        return userRepository.save(userEntity);
    }
    public UserEntity updateUserById(UpdateUserReqDTO updateUserReqDTO, UUID id) {
        Optional<UserEntity> optionalUserEntity=userRepository.findById(id);
        if(optionalUserEntity.isEmpty())
            throw new ApiRuntimeException("User does not exist",HttpStatus.NOT_FOUND);

        UserEntity userEntity=optionalUserEntity.get();
        userEntity.setFullName(updateUserReqDTO.getFullName());
        userEntity.setBio(updateUserReqDTO.getBio());

        return userRepository.save(userEntity);
    }

    public UserEntity updateEmailIdById(UpdateEmailIdReqDTO updateEmailIdReqDTO, UUID id) {
        Optional<UserEntity> optionalUserEntity=userRepository.findById(id);
        if(optionalUserEntity.isEmpty())
            throw new ApiRuntimeException("User does not exist",HttpStatus.NOT_FOUND);
        if(userRepository.existsByEmailId(updateEmailIdReqDTO.getEmailId()))
            throw new ApiRuntimeException("Email is already taken.",HttpStatus.ALREADY_REPORTED);

        UserEntity userEntity=optionalUserEntity.get();
        userEntity.setEmailId(updateEmailIdReqDTO.getEmailId());
        return userRepository.save(userEntity);
    }

    public UserEntity updatePasswordById(UpdatePasswordReqDTO updatePasswordReqDTO, UUID id) {

        Optional<UserEntity> optionalUserEntity=userRepository.findById(id);
        if(optionalUserEntity.isEmpty())
            throw new ApiRuntimeException("user does not exist", HttpStatus.NOT_FOUND);

        UserEntity userEntity=optionalUserEntity.get();
        userEntity.setPassword(passwordEncoder.encode(updatePasswordReqDTO.getPassword()));
        return userRepository.save(userEntity);
    }
}
