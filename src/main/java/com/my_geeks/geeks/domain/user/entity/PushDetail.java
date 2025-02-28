package com.my_geeks.geeks.domain.user.entity;

import com.my_geeks.geeks.domain.user.entity.enumeration.NotifyType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushDetail {
    @Id
    @Column(name = "notification_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long userId;

    @Column(length = 100)
    private String title;

    @Column(length = 100)
    private String body;

    @Enumerated(STRING)
    private NotifyType type;

    @Builder
    public PushDetail(Long userId, String title, String body, NotifyType type) {
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.type = type;
    }
}
