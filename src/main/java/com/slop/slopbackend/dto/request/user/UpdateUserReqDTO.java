package com.slop.slopbackend.dto.request.user;

import com.slop.slopbackend.utility.UserSchool;
import com.slop.slopbackend.utility.UserSpecialization;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateUserReqDTO {
    @NotEmpty
    @Size(min=3,max=32)
    private String fullName;

    @NotNull
    private String bio;

    @NotNull
    private UserSpecialization userSpecialization;
    @NotNull
    private UserSchool userSchool;
}
