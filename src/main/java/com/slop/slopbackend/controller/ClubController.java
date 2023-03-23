package com.slop.slopbackend.controller;

import com.slop.slopbackend.dto.response.club.AllClubsResDTO;
import com.slop.slopbackend.dto.response.club.ClubResDTO;
import com.slop.slopbackend.entity.ClubEntity;
import com.slop.slopbackend.service.ClubService;
import com.slop.slopbackend.utility.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("clubs")
public class ClubController {
    private final ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping
    public AllClubsResDTO getAllClubs() {
        List<ClubEntity> clubEntities = clubService.getAllClubs();
        List<ClubResDTO> clubResDTOS = new ArrayList<>();
        for (ClubEntity clubEntity : clubEntities) {
            ClubResDTO clubResDTO = ModelMapperUtil.toObject(clubEntity, ClubResDTO.class);
            clubResDTO.setProfilePicture(clubEntity.getUser().getProfilePicture());
            clubResDTOS.add(clubResDTO);
        }
        return AllClubsResDTO.builder()
                .clubs(clubResDTOS)
                .build();
    }
}

