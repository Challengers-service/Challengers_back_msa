package com.challengers.pointservice.point.dto;

import com.challengers.pointservice.point.domain.PointHistoryType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointHistoryResponse {
    private Long pointHistory;
    private String createdAt;
    private String type;

    @QueryProjection
    public PointHistoryResponse(Long pointHistory, LocalDateTime createdAt, PointHistoryType type) {
        this.pointHistory = pointHistory;
        this.createdAt = createdAt.toString();
        this.type = type.toString();
    }
}
