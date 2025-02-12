package com.my_geeks.geeks.domain.roommate.entity;

import com.my_geeks.geeks.customResponse.CreatedTime;
import com.my_geeks.geeks.domain.roommate.entity.enumeration.RoommateStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Roommate extends CreatedTime {
    @Id
    @Column(name = "roommate_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long senderId;

    private Long receiverId;

    private Long matchingPointId;

    @Enumerated(STRING)
    private RoommateStatus status;

    public Roommate(Long senderId, Long receiverId, Long matchingPointId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.matchingPointId = matchingPointId;
        this.status = RoommateStatus.PENDING;
    }
}
