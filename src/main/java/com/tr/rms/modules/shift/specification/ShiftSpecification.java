package com.tr.rms.modules.shift.specification;

import com.tr.rms.modules.shift.entity.Shift;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ShiftSpecification {


    public static Specification<Shift> hasShiftDate(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.equal(root.get("shiftDate"), date);
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
