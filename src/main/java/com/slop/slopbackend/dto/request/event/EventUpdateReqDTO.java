package com.slop.slopbackend.dto.request.event;

import lombok.Data;


import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
public class EventUpdateReqDTO {
    @Size(min=3)
    private String name;
    @Size(min=3)
    private String slug;
    @Size(min=3)
    private String descriptionMd;
    @Size(min=3)
    private String briefDescription;
    private Timestamp startTime;
    private Timestamp endTime;
}
