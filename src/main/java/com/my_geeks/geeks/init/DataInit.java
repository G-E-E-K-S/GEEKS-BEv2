package com.my_geeks.geeks.init;

import com.my_geeks.geeks.domain.roommate.service.RoommateService;
import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.UserDetail;
import com.my_geeks.geeks.domain.user.entity.enumeration.*;
import com.my_geeks.geeks.domain.user.repository.UserDetailRepository;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.domain.user.requestDto.CreateUserDetailReq;
import com.my_geeks.geeks.domain.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final UserService userService;

    private final RoommateService roommateService;

    private final UserRepository userRepository;

    private final UserDetailRepository userDetailRepository;

    private final BCryptPasswordEncoder encoder;

    //@PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));

        List<User> users = new ArrayList<>();

        users.add(User.builder()
                .email("1@sangmyung.kr")
                .password(encoder.encode("1234"))
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
                .password(encoder.encode("1234"))
                .nickname("TEST_2")
                .major("소프트웨어학과")
                .studentNum(25)
                .isOpen(true)
                .gender(Gender.MALE)
                .roleType(RoleType.ROLE_USER)
                .dormitory(Dormitory.NEW)
                .build());

        users.add(User.builder()
                .email("bak38393@naver.com")
                .password(encoder.encode("1234"))
                .nickname("TEST_3")
                .major("소프트웨어학과")
                .studentNum(21)
                .isOpen(true)
                .gender(Gender.MALE)
                .roleType(RoleType.ROLE_USER)
                .dormitory(Dormitory.NEW)
                .build());

        users.add(User.builder()
                .email("bak38394@naver.com")
                .password(encoder.encode("1234"))
                .nickname("TEST_4")
                .major("소프트웨어학과")
                .studentNum(23)
                .isOpen(true)
                .gender(Gender.MALE)
                .roleType(RoleType.ROLE_USER)
                .dormitory(Dormitory.NEW)
                .build());

        users.add(User.builder()
                .email("bak38395@naver.com")
                .password(encoder.encode("1234"))
                .nickname("TEST_5")
                .major("소프트웨어학과")
                .studentNum(20)
                .isOpen(true)
                .gender(Gender.MALE)
                .roleType(RoleType.ROLE_USER)
                .dormitory(Dormitory.NEW)
                .build());

//        for(int i = 6; i <= 1000; i++) {
//            users.add(User.builder()
//                    .email("bak38395@naver.com")
//                    .password(encoder.encode("1234"))
//                    .nickname("TEST_" + i)
//                    .major("소프트웨어학과")
//                    .studentNum(20)
//                    .isOpen(true)
//                    .gender(Gender.MALE)
//                    .roleType(RoleType.ROLE_USER)
//                    .dormitory(Dormitory.NEW)
//                    .build());
//        }

        users = userRepository.saveAll(users);

        CreateUserDetailReq req1 = CreateUserDetailReq.builder()
                .activityTime(ActivityTime.DAWN)
                .ear(Ear.DARK)
                .habit(Habit.NONHABIT)
                .smoke(Smoke.NONSMOKER)
                .outing(Outing.OUTSIDE)
                .tendency(Tendency.ALONE)
                .cleaning(Cleaning.DIRTY)
                .build();

        CreateUserDetailReq req2 = CreateUserDetailReq.builder()
                .activityTime(ActivityTime.DAWN)
                .ear(Ear.BRIGHT)
                .habit(Habit.HABIT)
                .smoke(Smoke.SMOKER)
                .outing(Outing.INSIDE)
                .tendency(Tendency.TOGETHER)
                .cleaning(Cleaning.CLEAN)
                .build();

        CreateUserDetailReq req3 = CreateUserDetailReq.builder()
                .activityTime(ActivityTime.DAWN)
                .ear(Ear.BRIGHT)
                .habit(Habit.HABIT)
                .smoke(Smoke.SMOKER)
                .outing(Outing.OUTSIDE)
                .tendency(Tendency.TOGETHER)
                .cleaning(Cleaning.CLEAN)
                .build();

        CreateUserDetailReq req4 = CreateUserDetailReq.builder()
                .activityTime(ActivityTime.MORNING)
                .ear(Ear.DARK)
                .habit(Habit.HABIT)
                .smoke(Smoke.NONSMOKER)
                .outing(Outing.OUTSIDE)
                .tendency(Tendency.ALONE)
                .cleaning(Cleaning.DIRTY)
                .build();

        CreateUserDetailReq req5 = CreateUserDetailReq.builder()
                .activityTime(ActivityTime.MORNING)
                .ear(Ear.BRIGHT)
                .habit(Habit.HABIT)
                .smoke(Smoke.NONSMOKER)
                .outing(Outing.OUTSIDE)
                .tendency(Tendency.ALONE)
                .cleaning(Cleaning.DIRTY)
                .build();
//        List<CreateUserDetailReq> reqs = new ArrayList<>();
//        reqs.add(req1);
//        reqs.add(req2);
//        reqs.add(req3);
//        reqs.add(req4);
//        reqs.add(req5);
        userService.createDetail(1L, req1);
        userService.createDetail(2L, req2);
        userService.createDetail(3L, req3);
        userService.createDetail(4L, req4);
        userService.createDetail(5L, req5);

//        for(Long i = 6L; i <= 1000L; i++) {
//            int index = (int) (i % 5);
//            userService.createDetail(i, reqs.get(index));
//        }

        roommateService.send(4L, 1L, 4L);
        roommateService.send(5L, 1L, 7L);
    }
}
