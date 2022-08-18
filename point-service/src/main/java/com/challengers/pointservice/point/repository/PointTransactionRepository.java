package com.challengers.pointservice.point.repository;

import com.challengers.pointservice.point.domain.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointTransactionRepository extends JpaRepository<PointTransaction,Long>, PointTransactionRepositoryCustom {
}
