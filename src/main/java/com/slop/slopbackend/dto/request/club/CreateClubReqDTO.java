package com.slop.slopbackend.dto.request.club;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CreateClubReqDTO {
    @NotEmpty
    @Size(min = 3, max = 20)
    private String clubName;

    @NotEmpty
    @Size(min = 3, max = 20)
    private String clubSlug;

    @NotEmpty
    @Size(min = 3, max =200)
    private String clubDescription;
}
