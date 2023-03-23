package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClubRepository extends JpaRepository<ClubEntity, UUID> {
    boolean existsByOwnerId(UUID userId);
    Optional<ClubEntity> findClubEntityByOwnerId(UUID userId);
    boolean existsByClubSlug(String clubSlug);
    Optional<ClubEntity> findClubEntityByClubSlug(String slug);
}
