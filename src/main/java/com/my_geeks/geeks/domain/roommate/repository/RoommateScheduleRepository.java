package com.my_geeks.geeks.domain.roommate.repository;

import com.my_geeks.geeks.domain.roommate.entity.RoommateSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateScheduleRepository extends JpaRepository<RoommateSchedule, Long> {
}
