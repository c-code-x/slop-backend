package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.EventCreatorEntity;
import com.slop.slopbackend.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventCreatorRepository extends JpaRepository<EventCreatorEntity, UUID> {
    @Query("SELECT ec.event FROM EventCreatorEntity ec WHERE ec.creator.user.id = :userId ORDER BY ec.event.createdAt DESC")
    List<EventEntity> findAllClubEventsByUserId(@Param("userId") UUID userId);
    @Query("SELECT ec.event from EventCreatorEntity ec WHERE ec.creator.clubSlug = :slug ORDER BY ec.event.createdAt DESC")
    List<EventEntity> findAllClubEventsBySlug(@Param("slug") String slug);
}
