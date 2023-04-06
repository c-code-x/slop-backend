package com.slop.slopbackend.service;

import com.slop.slopbackend.entity.ClubEntity;
import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.repository.ClubFollowerRepository;
import com.slop.slopbackend.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ClubService {
    final private ClubRepository clubRepository;
    private final ClubFollowerRepository clubFollowerRepository;

    @Autowired
    public ClubService(ClubRepository clubRepository,
                       ClubFollowerRepository clubFollowerRepository) {
        this.clubRepository = clubRepository;
        this.clubFollowerRepository = clubFollowerRepository;
    }
    public boolean existsByUserId(UUID userId){
        return clubRepository.existsByOwnerId(userId);
    }
    public ClubEntity findByUserId(UUID userId){
        Optional<ClubEntity> clubEntity= clubRepository.findClubEntityByOwnerId(userId);
        if(clubEntity.isEmpty())
            throw new ApiRuntimeException("Club does not exist!", HttpStatus.NOT_FOUND);
        clubEntity.get().getEvents().size();
        return clubEntity.get();
    }
    public ClubEntity saveClub(ClubEntity clubEntity, UserEntity userEntity){
        if(clubRepository.existsByClubSlug(clubEntity.getClubSlug()))
            throw new ApiRuntimeException("Club slug is already taken!", HttpStatus.ALREADY_REPORTED);
        clubEntity.setOwner(userEntity);
        return clubRepository.save(clubEntity);
    }
    public List<ClubEntity> getAllClubs(){
        return clubRepository.findAll().stream().peek(clubEntity -> clubEntity.getEvents().size()).toList();
    }

    public ClubEntity findClubById(UUID id) {
        ClubEntity clubEntity= clubRepository.findById(id).orElseThrow(() -> new ApiRuntimeException("Club does not exist!", HttpStatus.NOT_FOUND));
        clubEntity.getEvents().size();
        return clubEntity;
    }

    public ClubEntity getClubBySlug(String slug) {
        ClubEntity clubEntity= clubRepository.findClubEntityByClubSlug(slug).orElseThrow(() -> new ApiRuntimeException("Club does not exist!", HttpStatus.NOT_FOUND));
        clubEntity.getEvents().size();
        return clubEntity;
    }
    public boolean checkIfUserFollowsClub(ClubEntity clubEntity, UserEntity userEntity){
        return clubFollowerRepository.existsByClubAndUser(clubEntity,userEntity);
    }

    public boolean checkIfUserIsClubAdmin(UserEntity userEntity) {
        return clubRepository.existsByOwnerId(userEntity.getId());
    }
}
