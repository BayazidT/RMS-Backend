package com.tr.rms.modules.shift.service;

import com.tr.rms.modules.shift.dto.WeeklyScheduleRequest;
import com.tr.rms.modules.shift.dto.WeeklyScheduleResponse;
import com.tr.rms.modules.shift.entity.WeeklySchedule;
import com.tr.rms.modules.shift.repository.WeeklyScheduleRepository;
import com.tr.rms.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeWeeklyScheduleService {
    private final WeeklyScheduleRepository weeklyScheduleRepository;
    public WeeklyScheduleResponse create(UUID userId, WeeklyScheduleRequest req) {
        User user = User.builder().id(userId).build();


        List<WeeklySchedule> list = List.of(
                fromDay(req.monday(),    1, user),
                fromDay(req.tuesday(),   2, user),
                fromDay(req.wednesday(), 3, user),
                fromDay(req.thursday(),  4, user),
                fromDay(req.friday(),    5, user),
                fromDay(req.saturday(),  6, user),
                fromDay(req.sunday(),    7, user)
        );

        weeklyScheduleRepository.saveAll(list);

        return new WeeklyScheduleResponse();

    }

    private WeeklySchedule fromDay(WeeklyScheduleRequest.DaySchedule d, int dow, User user) {
        WeeklySchedule e = new WeeklySchedule();
        e.setUser(user);
        e.setDayOfWeek(dow);
        e.setStartTime(d.startTime());
        e.setEndTime(d.endTime());
        e.setOff(d.isOff());
        return e;
    }
}
