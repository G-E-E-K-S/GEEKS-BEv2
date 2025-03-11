package com.my_geeks.geeks.domain.roommate.service;

import com.my_geeks.geeks.actuator.ActuatorCounter;
import com.my_geeks.geeks.domain.roommate.entity.RoommateSchedule;
import com.my_geeks.geeks.domain.roommate.repository.RoommateScheduleRepository;
import com.my_geeks.geeks.domain.roommate.requestDto.CreateScheduleReq;
import com.my_geeks.geeks.domain.roommate.requestDto.UpdateScheduleReq;
import com.my_geeks.geeks.domain.roommate.responseDto.GetScheduleInfo;
import com.my_geeks.geeks.domain.roommate.responseDto.SchedulesOfDay;
import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.redis.CacheRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.my_geeks.geeks.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final UserRepository userRepository;
    private final RoommateScheduleRepository roommateScheduleRepository;

    private final CacheRepository cacheRepository;

    private final ActuatorCounter actuatorCounter;

    @Transactional
    public String create(Long userId, CreateScheduleReq req) {
        User user = getUser(userId);

        RoommateSchedule roommateSchedule = req.toEntity(user.getRoommateId(), userId);
        roommateScheduleRepository.save(roommateSchedule);
        actuatorCounter.scheduleCreateIncrement();
        return "success";
    }

    public List<SchedulesOfDay> getMonthSchedule(Long userId, int year, int month) {
        User user = cacheRepository.getUser(userId);
        YearMonth yearMonth = YearMonth.of(year, month);
        System.out.println("userId = " + userId);

        if(user.getRoommateId() == null) return null;

        LocalDateTime startDate = yearMonth.atDay(1).atTime(0, 0, 0);
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<GetScheduleInfo> monthSchedules = roommateScheduleRepository.findMonthSchedules(userId, user.getRoommateId(), startDate, endDate);
        List<SchedulesOfDay> calendar = new ArrayList<>(Collections.nCopies(endDate.getDayOfMonth() + 1, null));

        monthSchedules.forEach(schedule -> {
            LocalDateTime scheduleStart = schedule.getStartDate();
            LocalDateTime scheduleEnd = schedule.getEndDate();

            // 일정의 시작 달이 month 보다 이전이면 startDate
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

    public List<SchedulesOfDay> getWeekSchedule(Long userId) {
        User user = cacheRepository.getUser(userId);

        if(user.getRoommateId() == null) return null;

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        LocalDateTime startDate = startOfWeek.atTime(0, 0, 0);
        LocalDateTime endDate = endOfWeek.atTime(23, 59, 59);

        List<GetScheduleInfo> monthSchedules = roommateScheduleRepository.findMonthSchedules(userId, user.getRoommateId(), startDate, endDate);
        List<SchedulesOfDay> calendar = new ArrayList<>(Collections.nCopies(7, null));

        monthSchedules.forEach(schedule -> {
            LocalDateTime scheduleStart = schedule.getStartDate();
            LocalDateTime scheduleEnd = schedule.getEndDate();

            // 일정의 시작 달이 month 보다 이전이면 startDate
            LocalDateTime currentDate = scheduleStart.isBefore(startDate) ? startDate : scheduleStart;

            while (!currentDate.isAfter(scheduleEnd) && !currentDate.isAfter(endDate)) {
                int dayIndex = currentDate.getDayOfWeek().getValue() % 7;

                if (calendar.get(dayIndex) == null) {
                    calendar.set(dayIndex, new SchedulesOfDay());
                }

                // 해당 날짜에 일정 추가
                calendar.get(dayIndex).getSchedules().add(schedule);

                // 다음 날로 이동
                currentDate = currentDate.plusDays(1);
            }
        });

        return calendar;
    }

    @Transactional
    public String modify(Long userId, UpdateScheduleReq req) {
        RoommateSchedule schedule = getSchedule(req.getRoommateScheduleId());

        if(!schedule.getWriterId().equals(userId)) {
            throw new CustomException(WRITER_NOT_MATCHED);
        }

        schedule.updateSchedule(req);
        return "success";
    }

    @Transactional
    public String delete(Long userId, Long scheduleId) {
        RoommateSchedule schedule = getSchedule(scheduleId);

        if(!schedule.getWriterId().equals(userId)) {
            throw new CustomException(WRITER_NOT_MATCHED);
        }

        roommateScheduleRepository.delete(schedule);
        return "success";
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    private RoommateSchedule getSchedule(Long scheduleId) {
        return roommateScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND));
    }
}
