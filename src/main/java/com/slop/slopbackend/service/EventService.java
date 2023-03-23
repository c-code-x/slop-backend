package com.slop.slopbackend.service;

import com.slop.slopbackend.entity.*;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.repository.EventCreatorRepository;
import com.slop.slopbackend.repository.EventRepository;
import com.slop.slopbackend.repository.UserEventRepository;
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
    final private EventCreatorRepository eventCreatorRepository;
    final private UserEventRepository userEventRepository;
    final private ImageService imageService;
    @Autowired
    public EventService(EventRepository eventRepository, EventCreatorRepository eventCreatorRepository, UserEventRepository userEventRepository, ImageService imageService) {
        this.eventRepository = eventRepository;
        this.eventCreatorRepository = eventCreatorRepository;
        this.userEventRepository = userEventRepository;
        this.imageService = imageService;
    }

    public EventEntity saveEvent(EventEntity eventEntity, ClubEntity clubEntity) {
        if(eventRepository.existsBySlug(eventEntity.getSlug()))
            throw new ApiRuntimeException("Slug is already taken!", HttpStatus.ALREADY_REPORTED);

        EventCreatorEntity eventCreatorEntity=new EventCreatorEntity();
        eventCreatorEntity.setCreator(clubEntity);
        eventCreatorEntity.setEvent(eventRepository.save(eventEntity));
        return eventCreatorRepository.save(eventCreatorEntity).getEvent();
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

    public List<EventEntity> findAllClubEventsByUserId(UUID id) {
        return eventCreatorRepository.findAllClubEventsByUserId(id);
    }

    public void userEventInteraction(UserEntity userEntity, EventEntity eventEntity, UserEventAction userEventAction, boolean effect) {
        if(effect){
            UserEventEntity userEventEntity=UserEventEntity.builder()
                            .eventEntity(eventEntity)
                            .userEntity(userEntity)
                            .action(userEventAction)
                            .build();
            try {
                userEventRepository.save(userEventEntity);
            }catch (DataIntegrityViolationException e) {
                throw new ApiRuntimeException("User has already " + userEventAction + " this event!", HttpStatus.BAD_REQUEST);
            }
            return;
        }
        if(!userEventRepository.existsByUserEntityAndEventEntityAndAction(userEntity.getId(),eventEntity.getId(),userEventAction.toString()))
            throw new ApiRuntimeException("User has not "+ userEventAction +" this event!",HttpStatus.BAD_REQUEST);
        userEventRepository.deleteByUserEntityAndEventEntityAndAction(userEntity.getId(),eventEntity.getId(),userEventAction.toString());
    }
}
