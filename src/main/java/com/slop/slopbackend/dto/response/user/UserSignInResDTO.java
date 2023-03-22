package com.slop.slopbackend.dto.response.user;

import com.slop.slopbackend.utility.UserSchool;
import com.slop.slopbackend.utility.UserSpecialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInResDTO {
    private UUID id;
    private String registrationId;
    private String fullName;
    private String emailId;
    private String phoneNumber;
    private String bio;
    private String profilePicture;
    private String userRole;
    private UserSchool userSchool;
    private UserSpecialization userSpecialization;
    private String jwt;
}
