package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEntity, UUID> {
    boolean existsBySlug(String slug);

    Optional<EventEntity> findBySlug(String eventSlug);

//    @Query("SELECT ce FROM (SELECT cf.club FROM ClubFollowerEntity cf where cf.user.id = :userId) JOIN cf.club.clubEvents ce")
//    Object get(@Param("userId") UUID userId);
}
