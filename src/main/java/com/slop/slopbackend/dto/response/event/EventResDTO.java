package com.slop.slopbackend.dto.response.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResDTO {
    private UUID id;
    private String name;
    private String slug;
    private String descriptionMd;
    private String poster;
    private Timestamp startTime;
    private Timestamp endTime;
    private String briefDescription;
    private Timestamp createdAt;
}
