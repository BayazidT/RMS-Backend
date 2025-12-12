package com.tr.rms.modules.shift.dto;

import java.util.List;

public record ShiftListResponse(Long elements, int total, int page, int size, List<ShiftResponse> response) {
}
