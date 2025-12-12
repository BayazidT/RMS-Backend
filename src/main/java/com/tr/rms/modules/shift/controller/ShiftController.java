package com.tr.rms.modules.shift.controller;

import com.tr.rms.modules.shift.dto.ShiftListResponse;
import com.tr.rms.modules.shift.dto.ShiftRequest;
import com.tr.rms.modules.shift.dto.ShiftResponse;
import com.tr.rms.modules.shift.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/private/shift")
@RestController
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;


    @PostMapping("/")
    public ResponseEntity<String> create() {
        return ResponseEntity.ok(shiftService.createShifts());
    }

    @GetMapping("/today")
    public ShiftListResponse getTodayShifts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return shiftService.getShifts(page, size);
    }


    @PostMapping("/single/{userId}")
    public ShiftResponse createSingleShift(@RequestBody ShiftRequest shiftRequest, @PathVariable UUID userId) {
        return shiftService.create(shiftRequest, userId);
    }

    @GetMapping("/single/{userId}")
    public ShiftResponse getSingleShift(@PathVariable UUID userId) {
        return shiftService.getSingleShift(userId);
    }

}
