package com.tr.rms.modules.shift.service;

import com.tr.rms.exception.DuplicateEntryException;
import com.tr.rms.exception.DataNotFoundException;
import com.tr.rms.modules.shift.dto.ShiftListResponse;
import com.tr.rms.modules.shift.dto.ShiftRequest;
import com.tr.rms.modules.shift.dto.ShiftResponse;
import com.tr.rms.modules.shift.entity.Shift;
import com.tr.rms.modules.shift.entity.WeeklySchedule;
import com.tr.rms.modules.shift.repository.ShiftRepository;
import com.tr.rms.modules.shift.repository.WeeklyScheduleRepository;
import com.tr.rms.modules.shift.specification.ShiftSpecification;
import com.tr.rms.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final WeeklyScheduleRepository weeklyScheduleRepository;

    public String create(ShiftRequest shiftRequest, UUID userId) {
        Shift exsitShift = shiftRepository.findByUserIdAndShiftDate(userId, shiftRequest.shiftDate());
        if (exsitShift != null) {
            throw new DuplicateEntryException("Shift already exist for " + exsitShift.getUser().getName());
        }
        shiftRepository.save(mapToShiftEntity(shiftRequest,userId));
        return "Shift created";
    }

    private ShiftResponse toResponse(Shift shift) {
        return new ShiftResponse(
                shift.getId(),
                shift.getUser().getName(),
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

    public ShiftListResponse getShiftsByUserId(UUID userId, int page, int size, LocalDate fromDate, LocalDate toDate, String search) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<Shift> specification =
                ShiftSpecification.hasShiftDateRange(fromDate, toDate)
                        .and(ShiftSpecification.hasUserId(userId))
                        .and(ShiftSpecification.searchLike(search));
        Page<Shift> shiftPage = shiftRepository.findAll(specification, pageable);

        List<ShiftResponse> responses = shiftPage.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new ShiftListResponse(
                shiftPage.getTotalElements(),
                shiftPage.getTotalPages(),
                page,
                size,
                responses,
                shiftPage.isFirst(),
                shiftPage.isLast()
        );
    }

    public String createShifts(ShiftRequest req) {
        List<WeeklySchedule> weeklySchedules = weeklyScheduleRepository
                .findAllToday(getDayOfWeek(req.shiftDate()), false);

        if (weeklySchedules.isEmpty()) {
            throw new DataNotFoundException("No active weekly schedules found for the selected date");
        }

        List<Shift> existingShifts = shiftRepository
                .findByShiftDate(req.shiftDate());

        Set<UUID> userIdsWithExistingShift = existingShifts.stream()
                .map(shift -> shift.getUser().getId())
                .collect(Collectors.toSet());

        List<Shift> newShifts = new ArrayList<>();
        for (WeeklySchedule weeklySchedule : weeklySchedules) {
            UUID userId = weeklySchedule.getUser().getId();

            if (userIdsWithExistingShift.contains(userId)) {
                continue;
            }
            Shift shift = new Shift();
            shift.setUser(weeklySchedule.getUser());
            shift.setShiftDate(req.shiftDate());
            shift.setStartTime(req.startTime());
            shift.setEndTime(req.endTime());
            newShifts.add(shift);
        }
        if (!newShifts.isEmpty()) {
            shiftRepository.saveAll(newShifts);
            return "Created " + newShifts.size() + " new shift(s). Skipped duplicates.";
        } else {
            return "No new shifts created — all employees already have shifts for this date.";
        }
    }
    private int getDayOfWeek(LocalDate date){
        if(date.getDayOfWeek().getValue() > 2){
            return date.getDayOfWeek().getValue()-2;
        }
        return date.getDayOfWeek().getValue()+5;
    }

    public ShiftListResponse getShifts(int page, int size, LocalDate shiftDate, String search) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Shift> specification =
                ShiftSpecification.hasShiftDate(shiftDate)
                        .and(ShiftSpecification.searchLike(search));
        Page<Shift> shiftPage = shiftRepository.findAll(specification, pageable);

        List<ShiftResponse> responses = shiftPage.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new ShiftListResponse(
                shiftPage.getTotalElements(),
                shiftPage.getTotalPages(),
                page,
                size,
                responses,
                shiftPage.isFirst(),
                shiftPage.isLast()
        );
    }

}
