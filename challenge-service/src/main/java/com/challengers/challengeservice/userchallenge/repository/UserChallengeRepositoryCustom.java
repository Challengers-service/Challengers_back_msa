package com.challengers.challengeservice.userchallenge.repository;


import com.challengers.challengeservice.userchallenge.domain.UserChallenge;

import java.util.List;

public interface UserChallengeRepositoryCustom {
    Long updateStatusToSuccess(Long challengeId);

    long updateStatusToFail(List<Long> successIds);

    UserChallenge getChallengeById(Long userChallengeId);

    List<Long> getSuccessIds(boolean isMonday);

    Long getSumSuccessProgress(Long challengeId);
}
