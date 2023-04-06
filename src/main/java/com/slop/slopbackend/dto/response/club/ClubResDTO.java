package com.slop.slopbackend.dto.response.club;

import com.slop.slopbackend.dto.response.event.EventResDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubResDTO {
    private UUID id;
    private String clubName;
    private String clubSlug;
    private String clubDescription;
    private String profilePicture;
    private boolean userIsFollowing;
    private List<EventResDTO> events;
    public ClubResDTO(UUID id, String clubName, String clubSlug, String clubDescription, String profilePicture, boolean userIsFollowing) {
        this.id = id;
        this.clubName = clubName;
        this.clubSlug = clubSlug;
        this.clubDescription = clubDescription;
        this.profilePicture = profilePicture;
        this.userIsFollowing = userIsFollowing;
        this.events = new ArrayList<>();
    }
}
