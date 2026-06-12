package com.tr.rms.modules.shift.dto;

import java.util.List;

public record ShiftListResponse(Long totalElements, int totalPages, int pageNumber, int pageSize, List<ShiftResponse> content, boolean first,
                                boolean last) {
}
