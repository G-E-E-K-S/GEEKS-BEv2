package com.my_geeks.geeks.domain.roommate.service;

import com.my_geeks.geeks.domain.roommate.entity.RoommateSchedule;
import com.my_geeks.geeks.domain.roommate.repository.RoommateScheduleRepository;
import com.my_geeks.geeks.domain.roommate.requestDto.CreateScheduleReq;
import com.my_geeks.geeks.domain.roommate.responseDto.GetScheduleInfo;
import com.my_geeks.geeks.domain.roommate.responseDto.SchedulesOfDay;
import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    public List<SchedulesOfDay> getMonthSchedule(Long userId, int year, int month) {
        User user = getUser(userId);
        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDateTime startDate = yearMonth.atDay(1).atTime(0, 0, 0);
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        System.out.println("startDate = " + startDate + "endDate = " + endDate);

        List<GetScheduleInfo> monthSchedules = roommateScheduleRepository.findMonthSchedules(user.getRoommateId(), startDate, endDate);
        List<SchedulesOfDay> calendar = new ArrayList<>(Collections.nCopies(endDate.getDayOfMonth() + 1, null));

        monthSchedules.forEach(schedule -> {
            LocalDateTime scheduleStart = schedule.getStartDate();
            LocalDateTime scheduleEnd = schedule.getEndDate();

            // 일정의 시작 달이 month 보다 이전이면
            LocalDateTime currentDate = scheduleStart.isBefore(startDate) ? startDate : scheduleStart;

            while (!currentDate.isAfter(scheduleEnd) && !currentDate.isAfter(endDate)) {
                int day = currentDate.getDayOfMonth();

                if (calendar.get(day) == null) {
                    calendar.set(day, new SchedulesOfDay());
                }

                // 해당 날짜에 일정 추가
                calendar.get(day).getSchedules().add(schedule);

                // 다음 날로 이동
                currentDate = currentDate.plusDays(1);
            }
        });

        return calendar;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
