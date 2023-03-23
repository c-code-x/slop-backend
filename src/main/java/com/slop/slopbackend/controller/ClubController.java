package com.slop.slopbackend.controller;

import com.slop.slopbackend.dto.response.club.AllClubsResDTO;
import com.slop.slopbackend.dto.response.club.AllClubEventsResDTO;
import com.slop.slopbackend.dto.response.club.ClubResDTO;
import com.slop.slopbackend.dto.response.event.EventResDTO;
import com.slop.slopbackend.entity.ClubEntity;
import com.slop.slopbackend.service.ClubService;
import com.slop.slopbackend.utility.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            clubResDTO.setProfilePicture(clubEntity.getOwner().getProfilePicture());
            clubResDTOS.add(clubResDTO);
        }
        return AllClubsResDTO.builder()
                .clubs(clubResDTOS)
                .build();
    }

    @GetMapping("{slug}")
    public ClubResDTO getClubBySlug(@PathVariable String slug) {
        ClubEntity clubEntity = clubService.getClubBySlug(slug);
        ClubResDTO clubResDTO = ModelMapperUtil.toObject(clubEntity, ClubResDTO.class);
        clubResDTO.setProfilePicture(clubEntity.getOwner().getProfilePicture());
        return clubResDTO;
    }
    @GetMapping("{slug}/events")
    public AllClubEventsResDTO getClubAllEvents(@PathVariable String slug) {
        ClubEntity clubEntity = clubService.getClubBySlug(slug);
        return AllClubEventsResDTO.builder()
                .events(clubEntity.getClubEvents().stream().map(eventEntity -> ModelMapperUtil.toObject(eventEntity, EventResDTO.class)).toList())
                .build();
    }
}

