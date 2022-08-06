package com.challengers.challengeservice.challenge.repository;

import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.dto.ChallengeSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChallengeRepositoryCustom {
    Page<Challenge> search(ChallengeSearchCondition condition, Pageable pageable);

    long updateRound(boolean isMonday);

    long updateStatusFromReadyToInProgress();

    long updateStatusFromInProgressToValidate();

    long updateStatusFromValidateToFinish();
}
