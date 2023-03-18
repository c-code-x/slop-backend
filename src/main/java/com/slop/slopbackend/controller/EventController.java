package com.slop.slopbackend.controller;

import com.slop.slopbackend.dto.request.event.EventCreateReqDTO;
import com.slop.slopbackend.dto.request.event.EventUpdateReqDTO;
import com.slop.slopbackend.dto.response.event.EventCompleteResDTO;
import com.slop.slopbackend.dto.response.event.EventResDTO;
import com.slop.slopbackend.entity.EventCreatorEntity;
import com.slop.slopbackend.entity.EventEntity;
import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.repository.EventCreatorRepository;
import com.slop.slopbackend.service.EventService;
import com.slop.slopbackend.service.UserService;
import com.slop.slopbackend.utility.ModelMapperUtil;
import com.slop.slopbackend.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.sql.Timestamp;


@RestController
@RequestMapping("events")
@Transactional
public class EventController {
    private final EventService eventService;
    private final UserService userService;
    private final EventCreatorRepository eventCreatorRepository;

    @Autowired
    public EventController(EventService eventService, UserService userService,
                           EventCreatorRepository eventCreatorRepository) {
        this.eventService = eventService;
        this.userService = userService;
        this.eventCreatorRepository = eventCreatorRepository;
    }
    @PostMapping
    public EventResDTO createEvent(Authentication authentication, @RequestBody @Valid EventCreateReqDTO eventCreateReqDTO){
        validateStartTimeAndEndTime(eventCreateReqDTO.getStartTime(), eventCreateReqDTO.getEndTime());
        UserEntity userEntity=userService.getUserByEmailId(authentication.getName());
        if(!(userEntity.getUserRole()==UserRole.CLUB || userEntity.getUserRole()==UserRole.ADMIN)){
            throw new ApiRuntimeException("User cant create an event!", HttpStatus.UNAUTHORIZED);
        }
        EventEntity eventEntity=ModelMapperUtil.toEventEntity(eventCreateReqDTO);
        EventCreatorEntity eventCreatorEntity=new EventCreatorEntity();
        eventCreatorEntity.setEvent(eventEntity);
        eventCreatorEntity.setUser(userEntity);
        eventCreatorRepository.save(eventCreatorEntity);
        return ModelMapperUtil.toEventResDTO(eventService.saveEvent(eventEntity));
    }


//    @GetMapping
//    public List<EventCompleteResDTO> getClubEvents(Authentication authentication){
//        UserEntity userEntity=userService.getUserByEmailId(authentication.getName());
//        if(!(userEntity.getUserRole()==UserRole.CLUB || userEntity.getUserRole()==UserRole.ADMIN)){
//            throw new ApiRuntimeException("User is not authorized to access events", HttpStatus.UNAUTHORIZED);
//        }
//        Set<EventEntity> eventEntities=userEntity.getCreatedEvents();
//        System.out.println(userEntity);
//        System.out.println("SIZE: "+userEntity.getCreatedEvents().size());
//        return eventEntities.stream().map(ModelMapperUtil::toEventCompleteResDTO).toList();
//    }
    @GetMapping("{eventSlug}")
    public EventCompleteResDTO getEventBySlug(Authentication authentication,@PathVariable String eventSlug){
        UserEntity userEntity=userService.getUserByEmailId(authentication.getName());
        if(!(userEntity.getUserRole()==UserRole.CLUB || userEntity.getUserRole()==UserRole.ADMIN)){
            throw new ApiRuntimeException("User is not authorized to access events", HttpStatus.UNAUTHORIZED);
        }
        EventEntity eventEntity=eventService.getEventBySlug(eventSlug);
        return ModelMapperUtil.toEventCompleteResDTO(eventEntity);
    }
    @PatchMapping("{eventSlug}")
    public EventCompleteResDTO updateEventBySlug(Authentication authentication, @PathVariable String eventSlug, @RequestBody @Valid EventUpdateReqDTO eventUpdateReqDTO){
        validateStartTimeAndEndTime(eventUpdateReqDTO.getStartTime(),eventUpdateReqDTO.getEndTime());
        UserEntity userEntity=userService.getUserByEmailId(authentication.getName());
        if(!(userEntity.getUserRole()==UserRole.CLUB || userEntity.getUserRole()==UserRole.ADMIN)){
            throw new ApiRuntimeException("User is not authorized to access events", HttpStatus.UNAUTHORIZED);
        }
        EventEntity updatedEvent=eventService.updateEventBySlug(ModelMapperUtil.toEventEntity(eventUpdateReqDTO),eventSlug);
        return ModelMapperUtil.toEventCompleteResDTO(updatedEvent);
    }
    @DeleteMapping("{eventSlug}")
    public void deleteEventBySlug(@PathVariable String eventSlug,Authentication authentication){
        UserEntity userEntity=userService.getUserByEmailId(authentication.getName());
        if(!(userEntity.getUserRole()==UserRole.CLUB || userEntity.getUserRole()==UserRole.ADMIN)){
            throw new ApiRuntimeException("User is not authorized to access events", HttpStatus.UNAUTHORIZED);
        }
        eventService.deleteEventBySlug(eventSlug);
    }
    private void validateStartTimeAndEndTime(Timestamp startTime, Timestamp endTime){
        if(startTime==null && endTime!=null)
            throw new ApiRuntimeException("Start time is required when end time is present", HttpStatus.BAD_REQUEST);
        if(startTime!=null && endTime!=null && endTime.before(startTime))
            throw new ApiRuntimeException("End time cannot be before Start time", HttpStatus.BAD_REQUEST);
    }
}
