package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmailId(String emailId);
    boolean existsByEmailId(String emailId);
    boolean existsByRegistrationId(String registrationId);
}
