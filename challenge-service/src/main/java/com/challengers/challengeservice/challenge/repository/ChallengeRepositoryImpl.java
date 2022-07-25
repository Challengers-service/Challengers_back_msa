package com.challengers.challengeservice.challenge.repository;

import com.challengers.challengeservice.challenge.domain.Category;
import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.domain.ChallengeStatus;
import com.challengers.challengeservice.challenge.dto.ChallengeSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.challengers.challengeservice.challenge.domain.QChallenge.*;
import static com.challengers.challengeservice.challengetag.domain.QChallengeTag.*;
import static com.challengers.challengeservice.tag.domain.QTag.*;
import static org.springframework.util.StringUtils.hasText;

public class ChallengeRepositoryImpl implements ChallengeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChallengeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Challenge> search(ChallengeSearchCondition condition, Pageable pageable) {
        List<Challenge> result = queryFactory
                .select(challenge).distinct()
                .from(challenge)
                .join(challenge.challengeTags.challengeTags, challengeTag)
                .join(challengeTag.tag, tag)
                .where(searchCond(condition)
                        .and(challenge.status.in(ChallengeStatus.READY,ChallengeStatus.IN_PROGRESS)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(challenge.count())
                .from(challenge)
                .where(searchCond(condition));

        return PageableExecutionUtils.getPage(result,pageable,countQuery::fetchOne);
    }

    private BooleanBuilder searchCond(ChallengeSearchCondition condition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder
                .and(categoryEq(condition.getCategory()))
                .and(challengeNameContains(condition.getChallengeName()))
                .and(tagNameEq(condition.getTagName()));
    }

    private BooleanExpression challengeNameContains(String challengeName) {
        return hasText(challengeName) ? challenge.name.contains(challengeName) : null;
    }

    private BooleanExpression categoryEq(String category) {
        return hasText(category) ? challenge.category.eq(Category.of(category)) : null;
    }

    public BooleanExpression tagNameEq(String tagName) {
        return hasText(tagName) ? tag.name.eq(tagName) : null;
    }
}
