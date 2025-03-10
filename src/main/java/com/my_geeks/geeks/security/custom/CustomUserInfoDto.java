package com.my_geeks.geeks.security.custom;

import com.my_geeks.geeks.domain.user.entity.embedded.NotifyAllow;
import com.my_geeks.geeks.domain.user.entity.enumeration.Dormitory;
import com.my_geeks.geeks.domain.user.entity.enumeration.Gender;
import com.my_geeks.geeks.domain.user.entity.enumeration.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static jakarta.persistence.EnumType.STRING;

@Getter
@AllArgsConstructor
public class CustomUserInfoDto {
    private Long userId;

//    private String email;
//
//    private String password;
//
//    private String nickname;
//
//    private String major;
//
//    private String introduction;
//
//    private String image;
//
//    private String fcmToken;
//
//    private int studentNum;
//
//    private boolean isOpen;
//
//    @Embedded
//    private NotifyAllow notifyAllow;
//
//    private Dormitory dormitory;
//
//    private Gender gender;
//
//    private Long roommateId;

    private RoleType roleType;

    public CustomUserInfoDto(Long userId) {
        this.userId = userId;
    }
}
