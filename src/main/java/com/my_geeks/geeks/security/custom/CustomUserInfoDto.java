package com.my_geeks.geeks.security.custom;

import com.my_geeks.geeks.domain.user.entity.enumeration.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomUserInfoDto {
    private Long userId;

    private RoleType roleType;

    public CustomUserInfoDto(Long userId) {
        this.userId = userId;
    }
}
