package com.challengers.pointservice.point.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PointUpdateRequest {
    private Long pointHistory;
    private String pointHistoryType;
}
