package com.slop.slopbackend.controller;

import com.slop.slopbackend.dto.request.event.EventCreateReqDTO;
import com.slop.slopbackend.dto.request.event.EventUpdateReqDTO;
import com.slop.slopbackend.dto.response.event.EventCompleteResDTO;
import com.slop.slopbackend.dto.response.event.EventResDTO;
import com.slop.slopbackend.entity.ClubEntity;
import com.slop.slopbackend.entity.EventEntity;
import com.slop.slopbackend.entity.UserEntity;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.service.ClubService;
import com.slop.slopbackend.service.EventService;
import com.slop.slopbackend.service.ImageService;
import com.slop.slopbackend.service.UserService;
import com.slop.slopbackend.utility.CustomValidation;
import com.slop.slopbackend.utility.ModelMapperUtil;
import com.slop.slopbackend.utility.UserEventAction;
import com.slop.slopbackend.utility.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping("events")
@Transactional
public class EventController {
    private final EventService eventService;
    private final UserService userService;
    private final ImageService imageService;
    private final ClubService clubService;
    @Autowired
    public EventController(EventService eventService, UserService userService, ImageService imageService, ClubService clubService) {
        this.eventService = eventService;
        this.userService = userService;
        this.imageService = imageService;
        this.clubService = clubService;
    }
    @PostMapping
    public EventResDTO createEvent(Authentication authentication, @RequestParam String body, @RequestParam("poster") MultipartFile file){;
        if (file == null || file.isEmpty() || !Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new ApiRuntimeException("Invalid file", HttpStatus.BAD_REQUEST);
        }
        EventCreateReqDTO eventCreateReqDTO=ModelMapperUtil.json2Java(body,EventCreateReqDTO.class);
//       validation logic
        Validator validator= javax.validation.Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<EventCreateReqDTO>> violations=validator.validate(eventCreateReqDTO);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        validateStartTimeAndEndTime(eventCreateReqDTO.getStartTime(), eventCreateReqDTO.getEndTime());
        ClubEntity clubEntity=clubService.findByUserId(userService.getUserByEmailId(authentication.getName()).getId());
        eventCreateReqDTO.setPoster(imageService.saveImage(file));
        EventEntity eventEntity=eventService.saveEvent(ModelMapperUtil.toObject(eventCreateReqDTO,EventEntity.class), clubEntity);
        return ModelMapperUtil.toObject(eventEntity,EventResDTO.class);
    }


    @GetMapping
    public List<EventResDTO> getAllEvents(Authentication authentication){
        UserEntity userEntity=userService.getUserByEmailId(authentication.getName());
        List<EventEntity> eventEntities=eventService.findAllEvents();
        return eventEntities.stream().map(eventEntity -> eventService.getEventResDTO(eventEntity,userEntity)).toList();
    }

    @GetMapping("club")
    public List<EventCompleteResDTO> getClubEvents(Authentication authentication){
        UserEntity userEntity=userService.getUserByEmailId(authentication.getName());
        List<EventEntity> eventEntities=eventService.findAllClubEventsByOwnerId(userEntity.getId());
        return eventEntities.stream().map(ModelMapperUtil::toEventCompleteResDTO).toList();
    }
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
    @PostMapping("{eventSlug}/user/{userId}/interaction")
    public void userEventInteraction(@PathVariable String eventSlug, @PathVariable UUID userId, @RequestParam("action") String userEventActionString, @RequestParam(value = "effect",required = false,defaultValue = "true") boolean effect){
        if(!CustomValidation.isValidEnum(userEventActionString,UserEventAction.class))
            throw new ApiRuntimeException("Invalid user event action", HttpStatus.BAD_REQUEST);
        UserEntity userEntity=userService.getUserById(userId);
        EventEntity eventEntity=eventService.getEventBySlug(eventSlug);
        eventService.userEventInteraction(userEntity,eventEntity,UserEventAction.valueOf(userEventActionString),effect);
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
