package com.challengers.challengeservice.userchallenge.repository;

import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.dto.ChallengeSearchCondition;
import com.challengers.challengeservice.challenge.repository.ChallengeRepository;
import com.challengers.challengeservice.photocheck.domain.PhotoCheck;
import com.challengers.challengeservice.photocheck.repository.PhotoCheckRepository;
import com.challengers.challengeservice.userchallenge.domain.UserChallenge;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
class UserChallengeRepositoryTest {
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    UserChallengeRepository userChallengeRepository;
    @Autowired
    PhotoCheckRepository photoCheckRepository;

    Challenge challenge1;
    Challenge challenge2;
    @BeforeEach
    void setUp() {
        challenge1 = Challenge.builder()
                .id(1L)
                .name("매일 아침 7시 기상")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(10))
                .checkTimesPerRound(1)
                .userCount(1)
                .build();
        challengeRepository.save(challenge1);

        challenge2 = Challenge.builder()
                .id(2L)
                .name("매일")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(10))
                .checkTimesPerRound(1)
                .userCount(2)
                .build();
        challengeRepository.save(challenge2);

    }

}