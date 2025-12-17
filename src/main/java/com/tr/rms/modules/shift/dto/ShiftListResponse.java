package com.tr.rms.modules.shift.dto;

import java.util.List;

public record ShiftListResponse(Long elements, int totalPage, int currentPage, int size, List<ShiftResponse> response) {
}
