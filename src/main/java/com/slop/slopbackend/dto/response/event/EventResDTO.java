package com.slop.slopbackend.dto.response.event;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class EventResDTO {
    private UUID id;
    private String name;
    private String slug;
    private String descriptionMd;
    private Timestamp startTime;
    private Timestamp endTime;
    private String briefDescription;
}
