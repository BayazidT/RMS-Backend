// src/main/java/com/tr/rms/modules/reservation/dto/ReservationResponse.java
package com.tr.rms.modules.reservation.dto;

import com.tr.rms.modules.reservation.entity.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID userId,
        String username,
        Integer tableNumber,
        Integer guestCount,
        LocalDate reservationDate,
        LocalTime reservationTime,
        ReservationStatus status,
        String customerName,
        String customerPhone,
        String customerEmail,
        String specialRequests,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String notes
) {}