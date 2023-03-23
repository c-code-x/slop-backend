package com.slop.slopbackend.controller;

import com.slop.slopbackend.dto.request.club.CreateClubReqDTO;
import com.slop.slopbackend.dto.response.club.ClubResDTO;
import com.slop.slopbackend.entity.ClubEntity;
import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.service.ClubService;
import com.slop.slopbackend.service.UserService;
import com.slop.slopbackend.utility.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("admin")
@Transactional
public class AdminController {
    private final ClubService clubService;
    private final UserService userService;


    @Autowired
    public AdminController(ClubService clubService, UserService userService) {
        this.clubService = clubService;
        this.userService = userService;
    }
    @PostMapping("clubs")
    public ClubResDTO createClub(@RequestParam("userId") UUID userId, @RequestBody @Valid CreateClubReqDTO createClubReqDTO){
        UserEntity userEntity=userService.promoteUserToClub(userService.getUserById(userId));

        ClubEntity clubEntity= clubService.saveClub(ModelMapperUtil.toObject(createClubReqDTO,ClubEntity.class),userEntity);
        ClubResDTO clubResDTO= ModelMapperUtil.toObject(clubEntity,ClubResDTO.class);
        clubResDTO.setProfilePicture(clubEntity.getUser().getProfilePicture());
        return clubResDTO;
    }
}
