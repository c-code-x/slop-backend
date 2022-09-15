package com.slop.slopbackend.service;

import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String emailId) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmailId(emailId);
        if (optionalUserEntity.isEmpty()) throw new UsernameNotFoundException(emailId);
        UserEntity userEntity=optionalUserEntity.get();
        return new User(userEntity.getEmailId(), userEntity.getPassword(), List.of(new SimpleGrantedAuthority(userEntity.getUserRole().name())));
    }
}