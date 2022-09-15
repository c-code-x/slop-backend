package com.slop.slopbackend.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserSigninReqDTO {
    @NotEmpty
    @Email
    String emailId;

    @NotEmpty
    @Size(min=5,max=32)
    String password;
}
