package com.my_geeks.geeks.init;

import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.enumeration.Dormitory;
import com.my_geeks.geeks.domain.user.entity.enumeration.RoleType;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        User user = User.builder()
                .email("test@sangmyung.kr")
                .password("1234")
                .nickname("TEST_1")
                .major("소프트웨어학과")
                .studentNum(19)
                .isOpen(true)
                .roleType(RoleType.ROLE_USER)
                .dormitory(Dormitory.NEW)
                .build();

        userRepository.save(user);
    }
}
