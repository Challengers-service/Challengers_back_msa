package com.challengers.pointservice.point.dto;

import com.challengers.pointservice.point.domain.PointTransactionType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointTransactionResponse {
    private Long pointHistory;
    private String createdAt;
    private String type;

    @QueryProjection
    public PointTransactionResponse(Long pointHistory, LocalDateTime createdAt, PointTransactionType type) {
        this.pointHistory = pointHistory;
        this.createdAt = createdAt.toString();
        this.type = type.toString();
    }
}
