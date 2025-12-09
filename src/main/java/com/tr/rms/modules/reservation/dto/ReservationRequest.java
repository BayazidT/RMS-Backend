// src/main/java/com/tr/rms/modules/reservation/dto/ReservationRequest.java
package com.tr.rms.modules.reservation.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequest(

        @NotNull @FutureOrPresent(message = "Datum darf nicht in der Vergangenheit liegen")
        LocalDate reservationDate,

        @NotNull(message = "Uhrzeit erforderlich")
        LocalTime reservationTime,

        @NotNull @Min(1) @Max(20)
        Integer guestCount,

        @NotNull @Min(1)
        Integer tableNumber,

        @NotBlank(message = "Kundenname erforderlich")
        @Size(max = 100)
        String customerName,

        @Size(max = 30)
        String customerPhone,

        @Email @Size(max = 100)
        String customerEmail,

        @Size(max = 500)
        String specialRequests,

        String status
) {}