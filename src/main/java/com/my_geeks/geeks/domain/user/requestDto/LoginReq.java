package com.my_geeks.geeks.domain.user.requestDto;

import lombok.Getter;

@Getter
public class LoginReq {
    private String email;

    private String password;
}
