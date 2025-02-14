package com.my_geeks.geeks.domain.user.entity;

import com.my_geeks.geeks.customResponse.CreatedTime;
import com.my_geeks.geeks.domain.user.entity.enumeration.Dormitory;
import com.my_geeks.geeks.domain.user.entity.enumeration.Gender;
import com.my_geeks.geeks.domain.user.entity.enumeration.RoleType;
import com.my_geeks.geeks.domain.user.requestDto.UpdateProfileReq;
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
public class User extends CreatedTime {
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

    @Column(length = 100)
    private String image;

    private int studentNum;

    private boolean isOpen;

    @Enumerated(STRING)
    private RoleType roleType;

    @Enumerated(STRING)
    private Dormitory dormitory;

    @Enumerated(STRING)
    private Gender gender;

    @Builder
    public User(String email, String password, String nickname, String major, int studentNum, boolean isOpen, RoleType roleType, Dormitory dormitory, Gender gender) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.major = major;
        this.studentNum = studentNum;
        this.isOpen = isOpen;
        this.roleType = roleType;
        this.dormitory = dormitory;
        this.gender = gender;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void updateProfile(UpdateProfileReq req) {
        this.nickname = req.getNickname();
        this.major = req.getMajor();
        this.studentNum = req.getStudentNum();
        this.dormitory = req.getDormitory();
        this.introduction = req.getIntroduction();
    }
}
