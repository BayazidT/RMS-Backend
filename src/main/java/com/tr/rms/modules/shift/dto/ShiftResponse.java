package com.tr.rms.modules.shift.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ShiftResponse(
        UUID id,
        UUID userId,
        LocalDate shiftDate,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        LocalTime startTimeLocal,
        LocalTime endTimeLocal
) {}
