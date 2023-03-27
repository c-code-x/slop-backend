package com.slop.slopbackend.service;

import com.slop.slopbackend.dto.response.event.EventResDTO;
import com.slop.slopbackend.entity.*;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.repository.ClubRepository;
import com.slop.slopbackend.repository.EventRepository;
import com.slop.slopbackend.repository.UserEventRepository;
import com.slop.slopbackend.utility.ModelMapperUtil;
import com.slop.slopbackend.utility.UserEventAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EventService {

    final private EventRepository eventRepository;
    final private UserEventRepository userEventRepository;
    final private ImageService imageService;
    private final ClubRepository clubRepository;

    @Autowired
    public EventService(EventRepository eventRepository,  UserEventRepository userEventRepository, ImageService imageService,
                        ClubRepository clubRepository) {
        this.eventRepository = eventRepository;
        this.userEventRepository = userEventRepository;
        this.imageService = imageService;
        this.clubRepository = clubRepository;
    }

    public EventEntity saveEvent(EventEntity eventEntity, ClubEntity clubEntity) {
        if(eventRepository.existsBySlug(eventEntity.getSlug()))
            throw new ApiRuntimeException("Slug is already taken!", HttpStatus.ALREADY_REPORTED);
        eventEntity.setClub(clubEntity);
        return eventRepository.save(eventEntity);
    }

    public EventEntity getEventBySlug(String eventSlug) {
        Optional<EventEntity> optionalEventEntity= eventRepository.findBySlug(eventSlug);
        if(optionalEventEntity.isEmpty())
            throw new ApiRuntimeException("Event does not exist!",HttpStatus.NOT_FOUND);
        return optionalEventEntity.get();
    }

    public EventEntity updateEventBySlug(EventEntity oldEventEntity, String eventSlug) {
        EventEntity eventEntity=getEventBySlug(eventSlug);
        eventEntity.updateEventEntity(oldEventEntity);
        return eventRepository.save(eventEntity);
    }

    public void deleteEventBySlug(String eventSlug) {
        EventEntity eventEntity=getEventBySlug(eventSlug);
        eventRepository.delete(eventEntity);
    }

    public List<EventEntity> findAllClubEventsByOwnerId(UUID id) {
        return eventRepository.findAllEventsByClubOwnerId(id);
    }

    public void userEventInteraction(UserEntity userEntity, EventEntity eventEntity, UserEventAction userEventAction, boolean effect) {
        if(effect){
            UserEventEntity userEventEntity=UserEventEntity.builder()
                            .event(eventEntity)
                            .user(userEntity)
                            .action(userEventAction)
                            .build();
            try {
                userEventRepository.save(userEventEntity);
            }catch (DataIntegrityViolationException e) {
                throw new ApiRuntimeException("User has already " + userEventAction + " this event!", HttpStatus.BAD_REQUEST);
            }
            return;
        }
        if(!userEventRepository.existsByUserAndEventAndAction(userEntity,eventEntity,userEventAction))
            throw new ApiRuntimeException("User has not "+ userEventAction +" this event!",HttpStatus.BAD_REQUEST);
        userEventRepository.deleteByUserAndEventAndAction(userEntity,eventEntity,userEventAction);
    }

    public EventResDTO getEventResDTO(EventEntity eventEntity, UserEntity userEntity) {
        EventResDTO eventResDTO= ModelMapperUtil.toObject(eventEntity,EventResDTO.class);
        long numberOfLikes= userEventRepository.countByEventAndAction(eventEntity, UserEventAction.LIKED);
        long numberOfShares= userEventRepository.countByEventAndAction(eventEntity, UserEventAction.SHARED);
        long numberOfRegistrations= userEventRepository.countByEventAndAction(eventEntity, UserEventAction.REGISTERED);
        long numberOfAttendees= userEventRepository.countByEventAndAction(eventEntity, UserEventAction.ATTENDED);
        eventResDTO.setNumberOfLikes(numberOfLikes);
        eventResDTO.setNumberOfShares(numberOfShares);
        eventResDTO.setNumberOfRegistrations(numberOfRegistrations);
        eventResDTO.setNumberOfAttendees(numberOfAttendees);
        eventResDTO.setLiked(userEventRepository.existsByUserAndEventAndAction(userEntity,eventEntity,UserEventAction.LIKED));
        eventResDTO.setClubName(eventEntity.getClub().getClubName());
        eventResDTO.setClubProfilePicture(eventEntity.getClub().getOwner().getProfilePicture());
        eventResDTO.setClubSlug(eventEntity.getClub().getClubSlug());
        return eventResDTO;
    }

    public List<EventEntity> getEventsRegisteredByUser(UUID id) {
        return userEventRepository.findAllEventsRegisteredByUser(id);
    }
}
