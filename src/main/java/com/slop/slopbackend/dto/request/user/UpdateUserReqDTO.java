package com.slop.slopbackend.dto.request.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateUserReqDTO {
    @NotEmpty
    @Size(min=3,max=32)
    private String fullName;

    @NotEmpty
    @Size(min=3,max=32)
    private String registrationId;

    @NotNull
    private String bio;
}
