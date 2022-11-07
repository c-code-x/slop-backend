package com.slop.slopbackend.dto.response.event;

import com.slop.slopbackend.dto.response.user.UserResDTO;
import lombok.Data;

import java.util.Set;

@Data
public class EventCompleteResDTO extends EventResDTO{
    private Set<UserResDTO> registeredUsers;
    private Set<UserResDTO> eventCreators;
}
