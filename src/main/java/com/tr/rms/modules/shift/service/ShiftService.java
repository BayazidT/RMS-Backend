package com.tr.rms.modules.shift.service;

import com.tr.rms.modules.shift.dto.ShiftListResponse;
import com.tr.rms.modules.shift.dto.ShiftRequest;
import com.tr.rms.modules.shift.dto.ShiftResponse;
import com.tr.rms.modules.shift.entity.Shift;
import com.tr.rms.modules.shift.repository.ShiftRepository;
import com.tr.rms.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;
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
        shift.setUser(user); // lightweight reference
        shift.setShiftDate(req.shiftDate());
        shift.setStartTime(req.startTime());
        shift.setEndTime(req.endTime());
        return shift;
    }

    public ShiftResponse getSingleShift(UUID userId) {
        return toResponse(shiftRepository.findByUserId(userId));
    }

    public String createShifts() {
        return "Created shift for today";

    }

    public ShiftListResponse getShifts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Shift> shiftPage = shiftRepository.findAllToday(pageable);

        List<ShiftResponse> responses = shiftPage.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new ShiftListResponse(
                shiftPage.getTotalElements(),
                shiftPage.getTotalPages(),
                page,
                size,
                responses
        );
    }

}
