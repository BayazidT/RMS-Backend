package com.tr.rms.modules.shift.controller;

import com.tr.rms.modules.shift.dto.ShiftListResponse;
import com.tr.rms.modules.shift.dto.ShiftRequest;
import com.tr.rms.modules.shift.dto.ShiftResponse;
import com.tr.rms.modules.shift.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/private/shift")
@RestController
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;


    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody ShiftRequest req) {
        return ResponseEntity.ok(shiftService.createShifts(req));
    }

    @GetMapping("/list")
    public ShiftListResponse getTodayShifts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) LocalDate shiftDate,
            @RequestParam(required = false) String search
    ) {
        return shiftService.getShifts(page, size, shiftDate, search);
    }


    @PostMapping("/single/{userId}")
    public ShiftResponse createSingleShift(@RequestBody ShiftRequest shiftRequest, @PathVariable UUID userId) {
        return shiftService.create(shiftRequest, userId);
    }

    @GetMapping("/range/{userId}")
    public ShiftListResponse getShiftsByUserId(@PathVariable UUID userId,
                                                 @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(required = false) LocalDate fromDate,
    @RequestParam(required = false) LocalDate toDate,
    @RequestParam(required = false) String search

    ) {
        return shiftService.getShiftsByUserId(userId, page, size, fromDate, toDate, search);
    }

}
