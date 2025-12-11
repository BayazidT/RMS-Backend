package com.tr.rms.modules.shift.controller;

import com.tr.rms.modules.shift.dto.WeeklyScheduleRequest;
import com.tr.rms.modules.shift.dto.WeeklyScheduleResponse;
import com.tr.rms.modules.shift.service.EmployeeWeeklyScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/shift")
@RequiredArgsConstructor
public class EmployeeWeeklyScheduleController {

    private final EmployeeWeeklyScheduleService employeeWeeklyScheduleService;

    @PostMapping("/{userId}/weekly-schedule")
    public ResponseEntity<WeeklyScheduleResponse> weeklySchedule(@PathVariable UUID userId, @RequestBody WeeklyScheduleRequest request) {
        WeeklyScheduleResponse weeklyScheduleResponse = employeeWeeklyScheduleService.createOrUpdate(userId, request);
        return ResponseEntity.ok(weeklyScheduleResponse);
    }

    @GetMapping("/{userId}/weekly-schedule")
    public ResponseEntity<WeeklyScheduleResponse> weeklySchedule(@PathVariable UUID userId) {
        WeeklyScheduleResponse weeklyScheduleResponse = employeeWeeklyScheduleService.getWeeklySchedule(userId);
        return ResponseEntity.ok(weeklyScheduleResponse);

    }
}
