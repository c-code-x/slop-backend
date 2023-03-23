package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.EventEntity;
import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.entity.UserEventEntity;
import com.slop.slopbackend.utility.UserEventAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserEventRepository extends JpaRepository<UserEventEntity, UUID> {
//    boolean existsByUserAndEventAndAction(UUID userId, UUID eventId, String action);
    boolean existsByUserAndEventAndAction(UserEntity user, EventEntity event, UserEventAction action);
    void deleteByUserAndEventAndAction(UserEntity user, EventEntity event, UserEventAction action);
    long countByEventAndAction(EventEntity event, UserEventAction action);
}
