package com.tr.rms.modules.shift.specification;

import com.tr.rms.modules.shift.entity.Shift;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class ShiftSpecification {


    public static Specification<Shift> hasShiftDate(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.equal(root.get("shiftDate"), date);
    }

    public static Specification<Shift> hasUserId(UUID userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return null;
            }
            return cb.equal(root.get("user").get("id"), userId);
        };
    }


    public static Specification<Shift> hasShiftDateRange(
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return (root, query, cb) -> {

            if (fromDate == null && toDate == null) {
                return null;
            }

            if (fromDate != null && toDate != null) {
                return cb.between(root.get("shiftDate"), fromDate, toDate);
            }

            if (fromDate != null) {
                return cb.greaterThanOrEqualTo(root.get("shiftDate"), fromDate);
            }

            return cb.lessThanOrEqualTo(root.get("shiftDate"), toDate);
        };
    }


    public static Specification<Shift> searchLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern)
            );
        };
    }
}
