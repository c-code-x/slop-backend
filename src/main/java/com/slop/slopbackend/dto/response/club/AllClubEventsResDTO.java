package com.slop.slopbackend.dto.response.club;

import com.slop.slopbackend.dto.response.event.EventResDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AllClubEventsResDTO {
    List<EventResDTO> events;
}
