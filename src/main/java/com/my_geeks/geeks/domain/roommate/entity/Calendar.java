package com.my_geeks.geeks.domain.roommate.entity;

import com.my_geeks.geeks.domain.roommate.entity.enumeration.ScheduleType;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar {
    @Id
    @Column(name = "calendar_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long roommateId;

    @Column(length = 100)
    private String title;

    @DateTimeFormat(pattern = "YYYY.MM.DD HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "YYYY.MM.DD HH:mm")
    private LocalDateTime endDate;

    private ScheduleType type;

    private String description;
}
