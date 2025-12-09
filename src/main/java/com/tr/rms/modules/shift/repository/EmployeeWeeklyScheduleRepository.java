package com.tr.rms.modules.shift.repository;

import com.tr.rms.modules.shift.entity.EmployeeWeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeWeeklyScheduleRepository extends JpaRepository<EmployeeWeeklySchedule, UUID> {
}
