package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEntity, UUID> {
    boolean existsBySlug(String slug);

    Optional<EventEntity> findBySlug(String eventSlug);
}
