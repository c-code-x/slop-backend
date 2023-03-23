package com.slop.slopbackend.entity;

import com.slop.slopbackend.utility.UserEventAction;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_events",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "event_id","action"})
})
public class UserEventEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "user_id",nullable = false,updatable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="event_id",nullable = false,updatable = false)
    private EventEntity event;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserEventAction action;
}
