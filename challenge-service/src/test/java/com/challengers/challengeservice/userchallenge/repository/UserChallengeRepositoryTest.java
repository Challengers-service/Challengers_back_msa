package com.challengers.challengeservice.userchallenge.repository;

import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.repository.ChallengeRepository;
import com.challengers.challengeservice.photocheck.domain.PhotoCheck;
import com.challengers.challengeservice.photocheck.repository.PhotoCheckRepository;
import com.challengers.challengeservice.userchallenge.domain.UserChallenge;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    Challenge challenge;

    @BeforeEach
    void setUp() {
        challenge = Challenge.builder()
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(10))
                .checkTimesPerRound(1)
                .build();
        challengeRepository.save(challenge);

    }

    @Test
    void findAllFail() {
        UserChallenge userChallenge = UserChallenge.create(challenge, 1L);
        userChallengeRepository.save(userChallenge);

        List<PhotoCheck> photoChecks = userChallenge.getPhotoChecks();
        PhotoCheck photoCheck = PhotoCheck.builder().userChallenge(userChallenge).build();
        photoCheckRepository.save(photoCheck);
        photoChecks.add(photoCheck);

        List<UserChallenge> allFail = userChallengeRepository.findAllFail();

        Assertions.assertThat(allFail.size()).isEqualTo(0);
    }

    @Test
    void findAllFail2() {
        UserChallenge userChallenge1 = UserChallenge.create(challenge, 1L);
        userChallengeRepository.save(userChallenge1);
        UserChallenge userChallenge2 = UserChallenge.create(challenge, 2L);
        userChallengeRepository.save(userChallenge2);

        List<PhotoCheck> photoChecks = userChallenge1.getPhotoChecks();
        PhotoCheck photoCheck = PhotoCheck.builder().userChallenge(userChallenge1).build();
        photoCheckRepository.save(photoCheck);
        photoChecks.add(photoCheck);

        List<UserChallenge> allFail = userChallengeRepository.findAllFail();

        Assertions.assertThat(allFail.size()).isEqualTo(1);
    }
}