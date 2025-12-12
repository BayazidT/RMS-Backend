package com.tr.rms.modules.shift.service;

import com.tr.rms.modules.shift.dto.WeeklyScheduleRequest;
import com.tr.rms.modules.shift.dto.WeeklyScheduleRequest.DaySchedule;
import com.tr.rms.modules.shift.dto.WeeklyScheduleResponse;
import com.tr.rms.modules.shift.entity.WeeklySchedule;
import com.tr.rms.modules.shift.repository.WeeklyScheduleRepository;
import com.tr.rms.modules.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WeeklyScheduleService {

    private final WeeklyScheduleRepository weeklyScheduleRepository;

    // CREATE / UPDATE weekly pattern
    @Transactional
    public WeeklyScheduleResponse createOrUpdate(UUID userId, WeeklyScheduleRequest request) {
        User user = User.builder().id(userId).build();

        List<WeeklySchedule> schedules = List.of(
                fromDay(request.monday(),    1, user),
                fromDay(request.tuesday(),   2, user),
                fromDay(request.wednesday(), 3, user),
                fromDay(request.thursday(),  4, user),
                fromDay(request.friday(),    5, user),
                fromDay(request.saturday(),  6, user),
                fromDay(request.sunday(),    7, user)
        );

        // Optional: delete old ones first to avoid duplicates on update
        weeklyScheduleRepository.deleteAllByUserId(userId);
        weeklyScheduleRepository.saveAll(schedules);

        return buildResponse(schedules);
    }

    private WeeklySchedule fromDay(DaySchedule daySchedule, int dayOfWeek, User user) {
        WeeklySchedule e = new WeeklySchedule();
        e.setUser(user);
        e.setDayOfWeek(dayOfWeek);
        e.setStartTime(daySchedule.startTime());
        e.setEndTime(daySchedule.endTime());
        e.setOff(daySchedule.isOff());
        return e;
    }

    // READ weekly pattern
    public WeeklyScheduleResponse getWeeklySchedule(UUID userId) {
        List<WeeklySchedule> list = weeklyScheduleRepository.findAllByUserId(userId);

        return buildResponse(list);
    }

    private WeeklyScheduleResponse buildResponse(List<WeeklySchedule> schedules) {
        DaySchedule monday    = null;
        DaySchedule tuesday   = null;
        DaySchedule wednesday = null;
        DaySchedule thursday  = null;
        DaySchedule friday    = null;
        DaySchedule saturday = null;
        DaySchedule sunday    = null;

        for (WeeklySchedule ws : schedules) {
            DaySchedule day = toDaySchedule(ws.getDayOfWeek(), ws);
            switch (ws.getDayOfWeek()) {
                case 1 -> monday    = day;
                case 2 -> tuesday   = day;
                case 3 -> wednesday = day;
                case 4 -> thursday  = null;
                case 5 -> friday    = day;
                case 6 -> saturday   = day;
                case 7 -> sunday     = day;
            }
        }

        WeeklyScheduleRequest request = new WeeklyScheduleRequest(
                monday    != null ? monday    : defaultDay(1),
                tuesday   != null ? tuesday   : defaultDay(2),
                wednesday != null ? wednesday : defaultDay(3),
                thursday  != null ? thursday  : defaultDay(4),
                friday    != null ? friday    : defaultDay(5),
                saturday  != null ? saturday  : defaultDay(6),
                sunday    != null ? sunday    : defaultDay(7)
        );

        return new WeeklyScheduleResponse(request);
    }

    private DaySchedule toDaySchedule(int dayOfWeek, WeeklySchedule entity) {
        String dayNameGerman = switch (dayOfWeek) {
            case 1 -> "Montag";
            case 2 -> "Dienstag";
            case 3 -> "Mittwoch";
            case 4 -> "Donnerstag";
            case 5 -> "Freitag";
            case 6 -> "Samstag";
            case 7 -> "Sonntag";
            default -> "";
        };

        if (entity.isOff() || entity.getStartTime() == null) {
            return new DaySchedule(
                    dayOfWeek,
                    dayNameGerman,
                    null,
                    null,
                    true,
                    "Frei"
            );
        }

        String display = entity.getStartTime() + " – " + entity.getEndTime() + " Uhr";
        return new DaySchedule(
                dayOfWeek,
                dayNameGerman,
                entity.getStartTime(),
                entity.getEndTime(),
                false,
                display
        );
    }

    private DaySchedule defaultDay(int dayOfWeek) {
        String name = switch (dayOfWeek) {
            case 1 -> "Montag";
            case 2 -> "Dienstag";
            case 3 -> "Mittwoch";
            case 4 -> "Donnerstag";
            case 5 -> "Freitag";
            case 6 -> "Samstag";
            case 7 -> "Sonntag";
            default -> "";
        };

        return new DaySchedule(
                dayOfWeek,
                name,
                null,
                null,
                true,
                "Frei"
        );
    }
}