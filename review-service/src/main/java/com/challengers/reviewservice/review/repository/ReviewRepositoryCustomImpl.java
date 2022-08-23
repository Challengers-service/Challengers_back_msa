package com.challengers.reviewservice.review.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.challengers.reviewservice.review.domain.QReview.*;

@Repository
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public float getStarRatingAvgByChallengeId(Long challengeId) {
        Double starRatingAvg = queryFactory
                .select(review.starRating.avg())
                .from(review)
                .where(review.challengeId.eq(challengeId))
                .fetchOne();

        return starRatingAvg == null ? 0.0f : starRatingAvg.floatValue();
    }
}
