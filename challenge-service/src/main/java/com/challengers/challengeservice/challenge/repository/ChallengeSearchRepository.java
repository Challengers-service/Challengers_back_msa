package com.challengers.challengeservice.challenge.repository;

import com.challengers.challengeservice.challenge.domain.Category;
import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.dto.ChallengeSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import static com.challengers.challengeservice.challenge.domain.QChallenge.*;
import static com.challengers.challengeservice.challengetag.domain.QChallengeTag.*;
import static com.challengers.challengeservice.tag.domain.QTag.*;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class ChallengeSearchRepository extends Querydsl4RepositorySupport{

    public ChallengeSearchRepository() {
        super(Challenge.class);
    }

    public Page<Challenge> search(ChallengeSearchCondition condition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .select(challenge).distinct()
                        .from(challenge)
                        .join(challenge.challengeTags.challengeTags, challengeTag)
                        .join(challengeTag.tag, tag)
                        .where(searchCond(condition)),

                countQuery -> countQuery
                        .select(challenge).distinct()
                        .from(challenge)
                        .join(challenge.challengeTags.challengeTags, challengeTag)
                        .join(challengeTag.tag, tag)
                        .where(searchCond(condition))
        );
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
