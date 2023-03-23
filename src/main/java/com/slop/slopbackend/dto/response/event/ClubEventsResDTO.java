package com.slop.slopbackend.dto.response.event;

import lombok.Data;

import java.util.List;

@Data
public class ClubEventsResDTO {
    private List<EventResDTO> events;
}
