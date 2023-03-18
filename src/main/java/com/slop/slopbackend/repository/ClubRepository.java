package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClubRepository extends JpaRepository<ClubEntity, UUID> {
    boolean existsByUserId(UUID userId);
}
