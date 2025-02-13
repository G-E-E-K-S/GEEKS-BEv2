package com.my_geeks.geeks.domain.roommate.entity;

import com.my_geeks.geeks.customResponse.CreatedTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class RoommateBookmark extends CreatedTime {
    @Id
    @Column(name = "roommate_bookmark_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long myId;

    private Long opponentId;

    private Long matchingPointId;

    public RoommateBookmark(Long myId, Long opponentId, Long matchingPointId) {
        this.myId = myId;
        this.opponentId = opponentId;
        this.matchingPointId = matchingPointId;
    }
}
