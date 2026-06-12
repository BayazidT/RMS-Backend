package com.tr.rms.modules.shift.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record ShiftRequest(
        LocalDate shiftDate,
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {}
