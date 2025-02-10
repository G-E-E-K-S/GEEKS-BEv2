package com.my_geeks.geeks.domain.roommate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Roommate {
    @Id
    @Column(name = "roommate_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long senderId;

    private Long receiverId;

    public Roommate(Long senderId, Long receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
