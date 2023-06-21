package com.slop.slopbackend.repository;

import com.slop.slopbackend.dto.response.club.ClubResDTO;
import com.slop.slopbackend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClubFollowerRepository extends JpaRepository<ClubFollowerEntity, UUID> {
    boolean existsByClubAndUser(ClubEntity clubEntity, UserEntity userEntity);
    Optional<ClubFollowerEntity> findByClubAndUser(ClubEntity club, UserEntity user);
    List<ClubFollowerEntity> findByUser(UserEntity userEntity);

    @Query("SELECT "+
            " new com.slop.slopbackend.dto.response.club.ClubResDTO(c.club.id,c.club.clubName,c.club.clubSlug,c.club.clubDescription,c.club.owner.profilePicture,true) "+
            " FROM ClubFollowerEntity c WHERE c.user = ?1")
    List<ClubResDTO> findAllByUser(UserEntity userEntity);

//    @Query("SELECT c.club.clubEvents FROM ClubFollowerEntity c WHERE c.user = ?1")
//    List<EventCreatorEntity> findAllEventOfClubByUser(UserEntity userEntity);
}
