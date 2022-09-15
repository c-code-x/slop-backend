package com.slop.slopbackend.dto.response.user;

import com.slop.slopbackend.utility.UserRole;
import lombok.Data;


import java.util.UUID;

@Data
public class UserResDTO {
    private UUID id;

    private  String fullName;

    private String registrationId;

    private String emailId;

    private UserRole userRole;
}
