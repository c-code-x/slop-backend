package com.slop.slopbackend.entity;

import com.slop.slopbackend.exception.ApiRuntimeException;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String slug;

    private String poster;
    @Column(nullable = false,updatable = false)
    private Timestamp createdAt;
    private Timestamp startTime;

    private Timestamp endTime;

    @Column(nullable = false)
    @Lob
    private String descriptionMd;

    @Column(nullable = false)
    private String briefDescription;

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    List<UserEventEntity> userEventInteractions;

    @ManyToOne
    @JoinColumn(name = "club_id",referencedColumnName = "id",nullable = false)
    ClubEntity club;

    public void updateEventEntity(EventEntity eventEntity){
        if(eventEntity.getName()!=null)
            name=eventEntity.getName();
        if(eventEntity.getSlug()!=null)
            slug=eventEntity.getSlug();
        if(eventEntity.getStartTime()!=null)
            startTime=eventEntity.getStartTime();
        if(eventEntity.getEndTime()!=null)
            endTime=eventEntity.getEndTime();
        if(eventEntity.getDescriptionMd()!=null)
            descriptionMd=eventEntity.getDescriptionMd();
        if(eventEntity.getBriefDescription()!=null)
            briefDescription=eventEntity.getBriefDescription();
    }
    @PrePersist
    private void prePersist(){
        createdAt=new Timestamp(System.currentTimeMillis());
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || Hibernate.getClass(this) != Hibernate.getClass(object))
            return false;
        EventEntity that = (EventEntity) object;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
