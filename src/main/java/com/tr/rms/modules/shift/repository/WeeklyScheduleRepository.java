package com.tr.rms.modules.shift.repository;

import com.tr.rms.modules.shift.entity.WeeklySchedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WeeklyScheduleRepository extends JpaRepository<WeeklySchedule, UUID> {
    List<WeeklySchedule> findAllByUserId(UUID userId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM WeeklySchedule w WHERE w.user.id = :userId")
    void deleteAllByUserId(UUID userId);

    @Query("SELECT r FROM WeeklySchedule r WHERE r.dayOfWeek = :dayOfWeek")
    List<WeeklySchedule> findAllToday(int dayOfWeek);
}
