package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.ClubFollowerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClubFollowerRepository extends JpaRepository<ClubFollowerEntity, UUID> {
}
