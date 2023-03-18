package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.EventAttendeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventAttendeeRepository extends JpaRepository<EventAttendeeEntity, UUID> {
}
