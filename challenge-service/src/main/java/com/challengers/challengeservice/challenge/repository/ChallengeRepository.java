package com.challengers.challengeservice.challenge.repository;


import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.domain.ChallengeStatus;
import com.challengers.challengeservice.challenge.domain.CheckFrequencyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge,Long>, ChallengeRepositoryCustom {
    List<Challenge> findAllByEndDate(LocalDate EndDate);
}
