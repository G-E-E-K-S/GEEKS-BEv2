package com.my_geeks.geeks.init;

import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.enumeration.Dormitory;
import com.my_geeks.geeks.domain.user.entity.enumeration.Gender;
import com.my_geeks.geeks.domain.user.entity.enumeration.RoleType;
import com.my_geeks.geeks.domain.user.repository.UserDetailRepository;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.domain.user.requestDto.CreateUserDetailReq;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final UserRepository userRepository;

    private final UserDetailRepository userDetailRepository;

    @PostConstruct
    public void init() {
        List<User> users = new ArrayList<>();

        users.add(User.builder()
                .email("bak3839@naver.com")
                .password("1234")
                .nickname("TEST_1")
                .major("소프트웨어학과")
                .studentNum(19)
                .isOpen(true)
                .gender(Gender.MALE)
                .roleType(RoleType.ROLE_USER)
                .dormitory(Dormitory.NEW)
                .build());

        users.add(User.builder()
                .email("bak38392@naver.com")
                .password("1234")
                .nickname("TEST_3")
                .major("소프트웨어학과")
                .studentNum(25)
                .isOpen(true)
                .gender(Gender.MALE)
                .roleType(RoleType.ROLE_USER)
                .dormitory(Dormitory.NEW)
                .build());

        users.add(User.builder()
                .email("bak38393@naver.com")
                .password("1234")
                .nickname("TEST_4")
                .major("소프트웨어학과")
                .studentNum(21)
                .isOpen(true)
                .gender(Gender.MALE)
                .roleType(RoleType.ROLE_USER)
                .dormitory(Dormitory.NEW)
                .build());

        users.add(User.builder()
                .email("bak38394@naver.com")
                .password("1234")
                .nickname("TEST_5")
                .major("소프트웨어학과")
                .studentNum(23)
                .isOpen(true)
                .gender(Gender.MALE)
                .roleType(RoleType.ROLE_USER)
                .dormitory(Dormitory.NEW)
                .build());

        users = userRepository.saveAll(users);
    }
}
