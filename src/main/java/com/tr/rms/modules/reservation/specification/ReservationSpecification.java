package com.tr.rms.modules.reservation.specification;

import com.tr.rms.modules.reservation.entity.Reservation;
import com.tr.rms.modules.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ReservationSpecification {

    public static Specification<Reservation> hasStatus(ReservationStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Reservation> hasReservationDate(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.equal(root.get("reservationDate"), date);
    }

    public static Specification<Reservation> searchLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("customerName")), pattern),
                    cb.like(cb.lower(root.get("customerEmail")), pattern),
                    cb.like(cb.lower(root.get("customerPhone")), pattern),
                    cb.like(cb.lower(root.get("notes")), pattern)
            );
        };
    }
}
