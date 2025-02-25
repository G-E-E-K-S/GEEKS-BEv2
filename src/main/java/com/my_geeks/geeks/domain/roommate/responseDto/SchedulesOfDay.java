package com.my_geeks.geeks.domain.roommate.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SchedulesOfDay {
    List<GetScheduleInfo> schedules = new ArrayList<>();
}
