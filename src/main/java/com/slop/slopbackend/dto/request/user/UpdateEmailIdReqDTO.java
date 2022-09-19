package com.slop.slopbackend.dto.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UpdateEmailIdReqDTO {

    @NotEmpty
    @Email
    private String emailId;

}
