// src/main/java/com/tr/rms/modules/reservation/controller/ReservationController.java
package com.tr.rms.modules.reservation.controller;

import com.tr.rms.modules.reservation.dto.*;
import com.tr.rms.modules.reservation.entity.ReservationStatus;
import com.tr.rms.modules.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @PostMapping
//    @PreAuthorize("hasAuthority('reservation:create')")
    public ResponseEntity<ReservationResponse> create(
            @Valid @RequestBody ReservationRequest request,
            Authentication auth) {
        return ResponseEntity.ok(service.create(request, auth));
    }

    @GetMapping
    public ResponseEntity<ReservationListResponse> get(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) LocalDate reservationDate,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(
                service.getReservations(page, size, status, reservationDate, search)
        );
    }


    @GetMapping("/today")
    public ResponseEntity<List<ReservationResponse>> today() {
        return ResponseEntity.ok(service.getToday());
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ReservationResponse>> byDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.getByDate(date));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('reservation:update')")
    public ResponseEntity<ReservationResponse> updateStatus(
            @PathVariable UUID id,
            @RequestParam ReservationStatus status,
            Authentication auth) {
        return ResponseEntity.ok(service.updateStatus(id, status, auth));
    }
}