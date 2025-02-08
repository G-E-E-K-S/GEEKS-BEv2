package com.my_geeks.geeks.domain.matching.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingPoint {
    @Id
    @Column(name = "matching_point_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long smallUserId;
    private Long largeUserId;
    private int point;

    @Builder
    public MatchingPoint(Long smallUserId, Long largeUserId, int point) {
        this.smallUserId = smallUserId;
        this.largeUserId = largeUserId;
        this.point = point;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void changePoint(int point) {
        this.point = point;
    }
}
