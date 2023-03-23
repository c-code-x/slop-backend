package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.UserEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserEventRepository extends JpaRepository<UserEventEntity, UUID> {
    boolean existsByUserEntityAndEventEntityAndAction(UUID userId, UUID eventId, String action);
    void deleteByUserEntityAndEventEntityAndAction(UUID userId, UUID eventId, String action);
}
