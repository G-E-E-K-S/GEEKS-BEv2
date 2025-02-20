package com.my_geeks.geeks.domain.roommate.entity;

import com.my_geeks.geeks.domain.roommate.entity.enumeration.ScheduleType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoommateSchedule {
    @Id
    @Column(name = "roommate_schedule_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long roommateId;

    private Long writerId;

    @Column(length = 100)
    private String title;

    @DateTimeFormat(pattern = "yyyy.M.d HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy.M.d HH:mm")
    private LocalDateTime endDate;

    @Enumerated(STRING)
    private ScheduleType type;

    private String description;

    @Builder
    public RoommateSchedule(Long roommateId, Long writerId, String title, LocalDateTime startDate, LocalDateTime endDate, ScheduleType type, String description) {
        this.roommateId = roommateId;
        this.writerId = writerId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.description = description;
    }
}
