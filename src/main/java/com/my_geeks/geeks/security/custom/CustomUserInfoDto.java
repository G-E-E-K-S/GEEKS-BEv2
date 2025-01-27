package com.my_geeks.geeks.security.custom;

import head4.notify.domain.user.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomUserInfoDto {
    private Long userId;

    private Integer univId;

    private String email;

    private RoleType roleType;

    public CustomUserInfoDto(Long userId) {
        this.userId = userId;
    }
}
