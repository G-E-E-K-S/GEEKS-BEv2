package com.my_geeks.geeks.domain.user.entity;

import com.my_geeks.geeks.domain.user.entity.enumeration.Dormitory;
import com.my_geeks.geeks.domain.user.entity.enumeration.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 100)
    private String email;

    private String password;

    @Column(length = 100)
    private String nickname;

    @Column(length = 100)
    private String major;

    @Column(length = 100)
    private String introduction;

    private int studentNum;

    private boolean isOpen;

    @Enumerated(STRING)
    private RoleType roleType;

    @Enumerated(STRING)
    private Dormitory dormitory;

    @Builder
    public User(String email, String password, String nickname, String major, int studentNum, boolean isOpen, RoleType roleType, Dormitory dormitory) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.major = major;
        this.studentNum = studentNum;
        this.isOpen = isOpen;
        this.roleType = roleType;
        this.dormitory = dormitory;
    }
}
