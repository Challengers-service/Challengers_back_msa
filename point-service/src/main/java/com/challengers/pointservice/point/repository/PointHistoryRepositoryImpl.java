package com.challengers.pointservice.point.repository;

import com.challengers.pointservice.common.Querydsl4RepositorySupport;
import com.challengers.pointservice.point.domain.PointHistory;
import com.challengers.pointservice.point.dto.PointHistoryResponse;
import com.challengers.pointservice.point.dto.QPointHistoryResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static com.challengers.pointservice.point.domain.QPointHistory.pointHistory1;


public class PointHistoryRepositoryImpl extends Querydsl4RepositorySupport implements PointHistoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    public PointHistoryRepositoryImpl(EntityManager em) {
        super(PointHistory.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PointHistoryResponse> getPointHistory(Pageable pageable, Long pointId) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .select(new QPointHistoryResponse(
                                pointHistory1.pointHistory,
                                pointHistory1.createdAt,
                                pointHistory1.type
                        ))
                        .from(pointHistory1)
                        .where(pointHistory1.point.id.eq(pointId))
                        .orderBy(pointHistory1.id.desc()),

                countQuery -> countQuery
                        .select(pointHistory1.id)
                        .from(pointHistory1)
                        .where(pointHistory1.point.id.eq(pointId))
        );
    }

    @Override
    public void deleteByPointId(Long pointId) {
        queryFactory
                .delete(pointHistory1)
                .where(pointHistory1.point.id.eq(pointId))
                .execute();
    }
}
