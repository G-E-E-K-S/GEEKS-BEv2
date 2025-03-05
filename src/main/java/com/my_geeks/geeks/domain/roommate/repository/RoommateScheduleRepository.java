package com.my_geeks.geeks.domain.roommate.repository;

import com.my_geeks.geeks.domain.roommate.entity.RoommateSchedule;
import com.my_geeks.geeks.domain.roommate.responseDto.GetScheduleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RoommateScheduleRepository extends JpaRepository<RoommateSchedule, Long> {

    @Query("select new com.my_geeks.geeks.domain.roommate.responseDto.GetScheduleInfo(" +
            "rms.id, rms.title, rms.startDate, rms.endDate, rms.type, rms.description, u.nickname, u.image, " +
            "CASE WHEN rms.writerId = :userId THEN true ELSE false END) " +
            "from RoommateSchedule rms " +
            "join User u on rms.writerId = u.id " +
            "where rms.roommateId = :roommateId " +
            "and (rms.startDate between :startDate and :endDate or rms.endDate between :startDate and :endDate)")
    List<GetScheduleInfo> findMonthSchedules(Long userId, Long roommateId, LocalDateTime startDate, LocalDateTime endDate);
}
