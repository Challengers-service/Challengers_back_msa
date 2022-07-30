package com.challengers.pointservice.point.repository;

import com.challengers.pointservice.point.dto.PointHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointHistoryRepositoryCustom {
    Page<PointHistoryResponse> getPointHistory(Pageable pageable, Long pointId);

    void deleteByPointId(Long pointId);
}
