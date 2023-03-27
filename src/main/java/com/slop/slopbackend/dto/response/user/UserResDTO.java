package com.slop.slopbackend.dto.response.user;

import com.slop.slopbackend.dto.response.club.ClubResDTO;
import com.slop.slopbackend.dto.response.event.EventResDTO;
import com.slop.slopbackend.utility.UserRole;
import com.slop.slopbackend.utility.UserSchool;
import com.slop.slopbackend.utility.UserSpecialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResDTO {
    private UUID id;

    private  String fullName;

    private String registrationId;

    private String emailId;

    private String bio;

    private UserRole userRole;
    private String profilePicture;
    private UserSpecialization userSpecialization;
    private UserSchool userSchool;
    private List<ClubResDTO> clubsFollowedByUser;
    private List<EventResDTO> eventsRegisteredByUser;
}
