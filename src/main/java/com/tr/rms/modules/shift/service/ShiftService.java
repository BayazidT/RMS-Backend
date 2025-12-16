package com.tr.rms.modules.shift.service;

import com.tr.rms.modules.shift.dto.ShiftListResponse;
import com.tr.rms.modules.shift.dto.ShiftRequest;
import com.tr.rms.modules.shift.dto.ShiftResponse;
import com.tr.rms.modules.shift.entity.Shift;
import com.tr.rms.modules.shift.entity.WeeklySchedule;
import com.tr.rms.modules.shift.repository.ShiftRepository;
import com.tr.rms.modules.shift.repository.WeeklyScheduleRepository;
import com.tr.rms.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final WeeklyScheduleRepository weeklyScheduleRepository;

    public ShiftResponse create(ShiftRequest shiftRequest, UUID userId) {
        return toResponse(shiftRepository.save(mapToShiftEntity(shiftRequest,userId)));
    }

    private ShiftResponse toResponse(Shift shift) {
        return new ShiftResponse(
                shift.getId(),
                shift.getUser().getId(),
                shift.getShiftDate(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getStartTimeLocal(),
                shift.getEndTimeLocal()
        );
    }


    private Shift mapToShiftEntity(ShiftRequest req, UUID userId) {
        User user = new User();
        user.setId(userId);
        Shift shift = new Shift();
        shift.setUser(user);
        shift.setShiftDate(req.shiftDate());
        shift.setStartTime(req.startTime());
        shift.setEndTime(req.endTime());
        return shift;
    }

    public ShiftResponse getSingleShift(UUID userId) {
        return toResponse(shiftRepository.findByUserId(userId));
    }

    public String createShifts(ShiftRequest req) {
        List<WeeklySchedule> weeklySchedules =weeklyScheduleRepository.findAllToday(getDayOfWeek(req.shiftDate()), false);
        if(weeklySchedules.size()==0){
            throw new EmptyStackException();
        }
        List<Shift> shifts = new ArrayList<>();
        for (WeeklySchedule weeklySchedule : weeklySchedules) {
            Shift shift = new Shift();
            shift.setUser(weeklySchedule.getUser());
            shift.setShiftDate(req.shiftDate());
            shift.setStartTime(req.startTime());
            shift.setEndTime(req.endTime());
            shifts.add(shift);
        }
        shiftRepository.saveAll(shifts);
        return "Created shift for today";

    }
    private int getDayOfWeek(LocalDate date){
        if(date.getDayOfWeek().getValue() > 2){
            return date.getDayOfWeek().getValue()-2;
        }
        return date.getDayOfWeek().getValue()+5;
    }

    public ShiftListResponse getShifts(int page, int size) {
        page--;

        Pageable pageable = PageRequest.of(page, size);
        Page<Shift> shiftPage = shiftRepository.findAllToday(pageable);

        List<ShiftResponse> responses = shiftPage.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new ShiftListResponse(
                shiftPage.getTotalElements(),
                shiftPage.getTotalPages(),
                page+1,
                size,
                responses
        );
    }

}
