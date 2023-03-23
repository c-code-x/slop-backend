package com.slop.slopbackend.dto.response.user;

import com.slop.slopbackend.dto.response.event.EventResDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class UserFeedResDTO {
    private List<EventResDTO> events;
}
