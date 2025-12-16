package com.tr.rms.modules.reservation.dto;

import java.util.List;

public record ReservationListResponse(
        List<ReservationResponse> content,
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize,
        boolean first,
        boolean last) {
}
