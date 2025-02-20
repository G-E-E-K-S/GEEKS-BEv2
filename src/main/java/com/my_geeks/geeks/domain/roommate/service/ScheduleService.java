package com.my_geeks.geeks.domain.roommate.service;

import com.my_geeks.geeks.domain.roommate.entity.RoommateSchedule;
import com.my_geeks.geeks.domain.roommate.repository.RoommateScheduleRepository;
import com.my_geeks.geeks.domain.roommate.requestDto.CreateScheduleReq;
import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.my_geeks.geeks.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final UserRepository userRepository;
    private final RoommateScheduleRepository roommateScheduleRepository;

    @Transactional
    public String create(Long userId, CreateScheduleReq req) {
        User user = getUser(userId);

        RoommateSchedule roommateSchedule = req.toEntity(user.getRoommateId(), userId);
        roommateScheduleRepository.save(roommateSchedule);
        return "success";
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
