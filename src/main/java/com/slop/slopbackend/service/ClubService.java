package com.slop.slopbackend.service;

import com.slop.slopbackend.entity.ClubEntity;
import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.exception.ApiRuntimeException;
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

    @Autowired
    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }
    public boolean existsByUserId(UUID userId){
        return clubRepository.existsByOwnerId(userId);
    }
    public ClubEntity findByUserId(UUID userId){
        Optional<ClubEntity> clubEntity= clubRepository.findClubEntityByOwnerId(userId);
        if(clubEntity.isEmpty())
            throw new ApiRuntimeException("Club does not exist!", HttpStatus.NOT_FOUND);
        return clubEntity.get();
    }
    public ClubEntity saveClub(ClubEntity clubEntity, UserEntity userEntity){
        if(clubRepository.existsByClubSlug(clubEntity.getClubSlug()))
            throw new ApiRuntimeException("Club slug is already taken!", HttpStatus.ALREADY_REPORTED);
        clubEntity.setOwner(userEntity);
        return clubRepository.save(clubEntity);
    }
    public List<ClubEntity> getAllClubs(){
        return clubRepository.findAll();
    }

    public ClubEntity findClubById(UUID id) {
        return clubRepository.findById(id).orElseThrow(() -> new ApiRuntimeException("Club does not exist!", HttpStatus.NOT_FOUND));
    }

    public ClubEntity getClubBySlug(String slug) {
        return clubRepository.findClubEntityByClubSlug(slug).orElseThrow(() -> new ApiRuntimeException("Club does not exist!", HttpStatus.NOT_FOUND));
    }
}
