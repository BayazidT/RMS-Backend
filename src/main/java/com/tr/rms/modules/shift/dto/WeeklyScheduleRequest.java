package com.tr.rms.modules.shift.dto;

import java.time.LocalTime;
import java.util.UUID;
// 1. DTO for the weekly pattern (what you edit once per employee)
public record WeeklyScheduleRequest(
        // One entry per day – Monday = 1 … Sunday = 7
        DaySchedule monday,    // Tag 1
        DaySchedule tuesday,
        DaySchedule wednesday,
        DaySchedule thursday,
        DaySchedule friday,
        DaySchedule saturday,
        DaySchedule sunday

) {
    public record DaySchedule(
            int     dayOfWeek,              // 1 = Montag … 7 = Sonntag
            String  dayNameGerman,          // "Montag", "Dienstag" …
            LocalTime startTime,            // null = frei
            LocalTime endTime,              // null = frei
            boolean isOff,                   // true = frei
            String  displayText             // "10:00 – 18:00 Uhr" oder "Frei"
    ) {}
}