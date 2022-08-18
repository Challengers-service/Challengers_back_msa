package com.challengers.pointservice.point.repository;

import com.challengers.pointservice.point.dto.PointTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointTransactionRepositoryCustom {
    Page<PointTransactionResponse> getPointTransaction(Pageable pageable, Long pointId);

    void deleteByPointId(Long pointId);
}
