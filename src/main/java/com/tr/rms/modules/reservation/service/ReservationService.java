// src/main/java/com/tr/rms/modules/reservation/service/ReservationService.java
package com.tr.rms.modules.reservation.service;

import com.tr.rms.exception.DataNotFoundException;
import com.tr.rms.modules.reservation.dto.*;
import com.tr.rms.modules.reservation.entity.Reservation;
import com.tr.rms.modules.reservation.entity.ReservationStatus;
import com.tr.rms.modules.reservation.repository.ReservationRepository;
import com.tr.rms.modules.reservation.specification.ReservationSpecification;
import com.tr.rms.modules.user.entity.User;
import com.tr.rms.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReservationResponse create(ReservationRequest request, Authentication auth) {
        User user = getCurrentUser(auth);
        // Check if table is free
        boolean tableTaken = reservationRepository.findByDateAndTable(request.reservationDate(), request.tableNumber())
                .stream()
                .anyMatch(r -> r.getStatus() != ReservationStatus.CANCELLED && r.getStatus() != ReservationStatus.NO_SHOW);

        if (tableTaken) {
            throw new IllegalStateException("Tisch " + request.tableNumber() + " ist bereits reserviert");
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .status(ReservationStatus.valueOf(request.status()))
                .tableNumber(request.tableNumber())
                .guestCount(request.guestCount())
                .reservationDate(request.reservationDate())
                .reservationTime(request.reservationTime())
                .customerName(request.customerName())
                .customerPhone(request.customerPhone())
                .customerEmail(request.customerEmail())
                .specialRequests(request.specialRequests())
                .createdBy(user.getId())
                .build();

        Reservation saved = reservationRepository.save(reservation);
        return mapToResponse(saved, user.getUsername());
    }

    public List<ReservationResponse> getToday() {
        return reservationRepository.findByReservationDateOrderByReservationTimeAsc(LocalDate.now())
                .stream()
                .map(r -> mapToResponse(r, r.getUser().getUsername()))
                .toList();
    }

    public List<ReservationResponse> getByDate(LocalDate date) {
        return reservationRepository.findByReservationDateOrderByReservationTimeAsc(date)
                .stream()
                .map(r -> mapToResponse(r, r.getUser().getUsername()))
                .toList();
    }

    @Transactional
    public ReservationResponse updateStatus(UUID id, ReservationStatus status, Authentication auth) {
        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Reservierung nicht gefunden"));
        res.setStatus(status);
        return mapToResponse(reservationRepository.save(res), res.getUser().getUsername());
    }

    private User getCurrentUser(Authentication auth) {
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    private ReservationResponse mapToResponse(Reservation r, String username) {
        return new ReservationResponse(
                r.getId(),
                r.getUser().getId(),
                username,
                r.getTableNumber(),
                r.getGuestCount(),
                r.getReservationDate(),
                r.getReservationTime(),
                r.getStatus(),
                r.getCustomerName(),
                r.getCustomerPhone(),
                r.getCustomerEmail(),
                r.getSpecialRequests(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getNotes()
        );
    }

    public ReservationListResponse getReservations(
            int page,
            int size,
            ReservationStatus status,
            LocalDate reservationDate,
            String search
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Reservation> specification =
                ReservationSpecification.hasStatus(status)
                        .and(ReservationSpecification.hasReservationDate(reservationDate))
                        .and(ReservationSpecification.searchLike(search));

        Page<Reservation> reservations =
                reservationRepository.findAll(specification, pageable);

        return new ReservationListResponse(
                reservations.stream()
                        .map(r -> mapToResponse(r, r.getUser().getUsername()))
                        .toList(),
                reservations.getTotalElements(),
                reservations.getTotalPages(),
                reservations.getNumber(),
                reservations.getSize(),
                reservations.isFirst(),
                reservations.isLast()
        );
    }

}