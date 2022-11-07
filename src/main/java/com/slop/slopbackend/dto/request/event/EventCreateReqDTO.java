package com.slop.slopbackend.dto.request.event;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
public class EventCreateReqDTO {
    @NotEmpty
    @Size(min=3)
    private String name;
    @NotEmpty
    @Size(min=3)
    private String slug;
    @NotEmpty
    @Size(min=3)
    private String descriptionMd;
    @NotEmpty
    @Size(min=3)
    private String briefDescription;
    private Timestamp startTime;
    private Timestamp endTime;
}
