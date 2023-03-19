package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.EventCreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventCreatorRepository extends JpaRepository<EventCreatorEntity, UUID> {
}
