package com.challengers.pointservice.point.repository;

import com.challengers.pointservice.common.Querydsl4RepositorySupport;
import com.challengers.pointservice.point.domain.PointTransaction;
import com.challengers.pointservice.point.dto.PointTransactionResponse;
import com.challengers.pointservice.point.dto.QPointTransactionResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static com.challengers.pointservice.point.domain.QPointTransaction.*;


public class PointTransactionRepositoryImpl extends Querydsl4RepositorySupport implements PointTransactionRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    public PointTransactionRepositoryImpl(EntityManager em) {
        super(PointTransaction.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PointTransactionResponse> getPointTransaction(Pageable pageable, Long pointId) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .select(new QPointTransactionResponse(
                                pointTransaction.pointHistory,
                                pointTransaction.createdAt,
                                pointTransaction.type
                        ))
                        .from(pointTransaction)
                        .where(pointTransaction.point.id.eq(pointId))
                        .orderBy(pointTransaction.id.desc()),

                countQuery -> countQuery
                        .select(pointTransaction.id)
                        .from(pointTransaction)
                        .where(pointTransaction.point.id.eq(pointId))
        );
    }

    @Override
    public void deleteByPointId(Long pointId) {
        queryFactory
                .delete(pointTransaction)
                .where(pointTransaction.point.id.eq(pointId))
                .execute();
    }
}
