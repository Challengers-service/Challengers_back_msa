package com.challengers.pointservice.point.repository;

import com.challengers.pointservice.point.domain.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory,Long>, PointHistoryRepositoryCustom {
}
