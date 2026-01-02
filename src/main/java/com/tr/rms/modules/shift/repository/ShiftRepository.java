package com.tr.rms.modules.shift.repository;

import com.tr.rms.modules.shift.entity.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    List<Shift> findByUserId(UUID userId);

    @Query("SELECT s FROM Shift s WHERE s.shiftDate = CURRENT_DATE")
    Page<Shift> findAllToday(Pageable pageable);

    Page<Shift> findAll(Specification<Shift> specification, Pageable pageable);

    @Query("SELECT s FROM Shift s WHERE s.shiftDate =:dayOfWeek")
    List<Shift> findAllShiftDate(int dayOfWeek);

    List<Shift> findByShiftDate(LocalDate localDate);

    Shift findByUserIdAndShiftDate(UUID userId, LocalDate localDate);
}
