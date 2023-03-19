package com.slop.slopbackend.service;

import com.slop.slopbackend.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class ClubService {
    final private ClubRepository clubRepository;

    @Autowired
    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }
    public boolean existsByUserId(UUID   userId){
        return clubRepository.existsByUserId(userId);
    }
}
