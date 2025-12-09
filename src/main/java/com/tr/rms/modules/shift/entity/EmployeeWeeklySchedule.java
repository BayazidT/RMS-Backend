package com.tr.rms.modules.shift.entity;

import com.tr.rms.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "employee_weekly_schedule",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "day_of_week"}))
@Getter
@Setter
@NoArgsConstructor
public class EmployeeWeeklySchedule {

    @Id
    @GeneratedValue
    @Column(name = "schedule_id", updatable = false, columnDefinition = "uuid")
    private UUID scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "day_of_week", nullable = false)
    private int dayOfWeek;               // 0 = Sunday … 6 = Saturday

    @Column(name = "start_time")
    private LocalTime startTime;         // null when isOff = true

    @Column(name = "end_time")
    private LocalTime endTime;           // null when isOff = true

    @Column(name = "is_off", nullable = false)
    private boolean off = false;
}