package com.my_geeks.geeks.domain.user.entity;

import com.my_geeks.geeks.customResponse.BaseTime;
import com.my_geeks.geeks.domain.user.entity.enumeration.*;
import com.my_geeks.geeks.domain.user.requestDto.CreateUserDetailReq;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import static jakarta.persistence.EnumType.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetail extends BaseTime implements Persistable<Long> {
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

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public void updateDetail(CreateUserDetailReq req) {
        this.ear = req.getEar();
        this.smoke = req.getSmoke();
        this.habit = req.getHabit();
        this.activityTime = req.getActivityTime();
        this.outing = req.getOuting();
        this.cleaning = req.getCleaning();
        this.tendency = req.getTendency();
    }
}
