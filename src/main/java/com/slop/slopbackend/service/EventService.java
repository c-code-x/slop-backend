package com.slop.slopbackend.service;

import com.slop.slopbackend.entity.EventEntity;
import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class EventService {

    final private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventEntity saveEvent(EventEntity eventEntity) {
        if(eventRepository.existsBySlug(eventEntity.getSlug()))
            throw new ApiRuntimeException("Slug is already taken!", HttpStatus.ALREADY_REPORTED);
        return  eventRepository.save(eventEntity);
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
}
