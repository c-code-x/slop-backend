package com.slop.slopbackend.controller;

import com.slop.slopbackend.dto.response.club.AllClubsResDTO;
import com.slop.slopbackend.dto.response.club.AllClubEventsResDTO;
import com.slop.slopbackend.dto.response.club.ClubResDTO;
import com.slop.slopbackend.dto.response.event.EventResDTO;
import com.slop.slopbackend.entity.ClubEntity;
import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.service.ClubService;
import com.slop.slopbackend.service.EventService;
import com.slop.slopbackend.service.UserService;
import com.slop.slopbackend.utility.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("clubs")
public class ClubController {
    private final ClubService clubService;
    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public ClubController(ClubService clubService, EventService eventService, UserService userService) {
        this.clubService = clubService;
        this.eventService = eventService;
        this.userService = userService;
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
    public ClubResDTO getClubBySlug(@PathVariable String slug, Authentication authentication) {
        UserEntity userEntity=userService.getUserByEmailId(authentication.getName());
        ClubEntity clubEntity = clubService.getClubBySlug(slug);
        ClubResDTO clubResDTO = ModelMapperUtil.toObject(clubEntity, ClubResDTO.class);
        clubResDTO.setProfilePicture(clubEntity.getOwner().getProfilePicture());
        clubResDTO.setEvents(clubEntity.getEvents().stream().map(eventEntity -> eventService.getEventResDTO(eventEntity,userEntity)).toList());
        clubResDTO.setUserIsFollowing(clubService.checkIfUserFollowsClub(clubEntity,userEntity));
        return clubResDTO;
    }
    @GetMapping("{slug}/events")
    public AllClubEventsResDTO getClubAllEvents(@PathVariable String slug) {
        ClubEntity clubEntity = clubService.getClubBySlug(slug);
        return AllClubEventsResDTO.builder()
                .events(clubEntity.getEvents().stream().map(eventEntity -> ModelMapperUtil.toObject(eventEntity, EventResDTO.class)).toList())
                .build();
    }
}

