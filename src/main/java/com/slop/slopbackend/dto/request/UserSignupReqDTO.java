package com.slop.slopbackend.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserSignupReqDTO {
    @NotEmpty
    @Size(min=3,max=32)
    String fullName;

    @NotEmpty
    @Size(min=3,max=32)
    String registrationId;

    @NotEmpty
    @Email
    String emailId;

    @NotEmpty
    @Size(min=10,max=10)
    String phoneNumber;

    @NotEmpty
    @Size(min=5,max=32)
    String password;
}
