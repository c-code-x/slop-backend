package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClubRepository extends JpaRepository<ClubEntity, UUID> {
    boolean existsByUserId(UUID userId);
    Optional<ClubEntity> findClubEntityByUserId(UUID userId);
    boolean existsByClubSlug(String clubSlug);
}
