package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.EventRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, UUID> {
}
