package com.tr.rms.modules.shift.repository;

import com.tr.rms.modules.shift.entity.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    Shift findByUserId(UUID userId);

    @Query("SELECT s FROM Shift s WHERE s.shiftDate = CURRENT_DATE")
    Page<Shift> findAllToday(Pageable pageable);

}
