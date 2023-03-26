package com.slop.slopbackend.repository;

import com.slop.slopbackend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClubFollowerRepository extends JpaRepository<ClubFollowerEntity, UUID> {
    boolean existsByClubAndUser(ClubEntity clubEntity, UserEntity userEntity);
    Optional<ClubFollowerEntity> findByClubAndUser(ClubEntity club, UserEntity user);

//    @Query("SELECT c.club.clubEvents FROM ClubFollowerEntity c WHERE c.user = ?1")
//    List<EventCreatorEntity> findAllEventOfClubByUser(UserEntity userEntity);
}
