package com.slop.slopbackend.dto.response.club;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllClubsResDTO {
    List<ClubResDTO> clubs;
}
