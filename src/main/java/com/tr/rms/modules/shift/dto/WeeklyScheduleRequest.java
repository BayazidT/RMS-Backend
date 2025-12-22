package com.tr.rms.modules.shift.dto;

import java.time.LocalTime;

public record WeeklyScheduleRequest(
        DaySchedule monday,
        DaySchedule tuesday,
        DaySchedule wednesday,
        DaySchedule thursday,
        DaySchedule friday,
        DaySchedule saturday,
        DaySchedule sunday
) {

    public static WeeklyScheduleRequest defaultSchedule() {
        return new WeeklyScheduleRequest(
                workingDay(1, "Montag", "10:00", "18:00"),
                workingDay(2, "Dienstag", "11:00", "18:00"),
                workingDay(3, "Mittwoch", "12:00", "18:00"),
                workingDay(4, "Donnerstag", "10:00", "18:00"),
                workingDay(5, "Freitag", "10:00", "22:00"),
                offDay(6, "Samstag"),
                offDay(7, "Sonntag")
        );
    }

    private static DaySchedule workingDay(
            int dayOfWeek,
            String dayNameGerman,
            String start,
            String end
    ) {
        return new DaySchedule(
                dayOfWeek,
                dayNameGerman,
                LocalTime.parse(start),
                LocalTime.parse(end),
                false,
                start + " – " + end + " Uhr"
        );
    }

    private static DaySchedule offDay(
            int dayOfWeek,
            String dayNameGerman
    ) {
        return new DaySchedule(
                dayOfWeek,
                dayNameGerman,
                null,
                null,
                true,
                "Frei"
        );
    }

    public record DaySchedule(
            int dayOfWeek,
            String dayNameGerman,
            LocalTime startTime,
            LocalTime endTime,
            boolean isOff,
            String displayText
    ) { }
}
