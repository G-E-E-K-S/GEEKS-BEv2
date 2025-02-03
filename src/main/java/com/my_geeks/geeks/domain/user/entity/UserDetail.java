package com.my_geeks.geeks.domain.user.entity;

import com.my_geeks.geeks.domain.user.entity.enumeration.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetail {
    @Id
    @Column(name = "user_detail_id")
    private Long id;

    @Column(length = 50)
    @Enumerated(STRING)
    private Smoke smoke;

    @Column(length = 50)
    @Enumerated(STRING)
    private Habit habit;

    @Column(length = 50)
    @Enumerated(STRING)
    private Ear ear;

    @Column(length = 50)
    @Enumerated(STRING)
    private ActivityTime activityTime;

    @Column(length = 50)
    @Enumerated(STRING)
    private Outing outing;

    @Column(length = 50)
    @Enumerated(STRING)
    private Cleaning cleaning;

    @Column(length = 50)
    @Enumerated(STRING)
    private Tendency tendency;
}
