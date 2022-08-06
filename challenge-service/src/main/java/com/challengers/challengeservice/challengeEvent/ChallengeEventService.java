package com.challengers.challengeservice.challengeEvent;

import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.repository.ChallengeRepository;
import com.challengers.challengeservice.global.client.PointClient;
import com.challengers.challengeservice.global.dto.GiveRewardDto;
import com.challengers.challengeservice.userchallenge.domain.UserChallenge;
import com.challengers.challengeservice.userchallenge.domain.UserChallengeStatus;
import com.challengers.challengeservice.userchallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ChallengeEventService {
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;

    private final PointClient pointClient;

    @Transactional
    public void challengeUpdateEvent() {
        boolean isMonday = LocalDate.now().getDayOfWeek().getValue() == 1;

        challengeRepository.updateStatusFromValidateToFinish();

        success();

        fail(isMonday);

        challengeRepository.updateStatusFromInProgressToValidate();

        challengeRepository.updateStatusFromReadyToInProgress();

        challengeRepository.updateRound(isMonday);
    }

    //TODO : 포인트 지급을 벌크 업데이트로 변경
    private void success() {
        LocalDate validateStartDate = LocalDate.now().minusDays(7);
        List<Challenge> findChallenges = challengeRepository.findAllByEndDate(validateStartDate);
        for (Challenge challenge : findChallenges) {
            userChallengeRepository.updateStatusToSuccess(challenge.getId());
            Long sumSuccessProgress = userChallengeRepository.getSumSuccessProgress(challenge.getId());

            long rewardUnit = (long) Math.floor(challenge.getFailedPoint() * 1.0 / sumSuccessProgress);

            List<UserChallenge> successUsers = userChallengeRepository.findByChallengeIdAndStatus(challenge.getId(), UserChallengeStatus.SUCCESS);

            for (UserChallenge successUser : successUsers) {
                pointClient.giveReward(new GiveRewardDto(successUser.getUserId(),successUser.getMaxProgress() * rewardUnit + challenge.getDepositPoint()));
            }

        }
    }

    private void fail(boolean isMonday) {
        List<Long> successUserChallengeIds = userChallengeRepository.getSuccessIds(isMonday);
        userChallengeRepository.updateStatusToFail(successUserChallengeIds);
        for (Long userChallengeId : successUserChallengeIds) {
            UserChallenge userChallenge = userChallengeRepository.getChallengeById(userChallengeId);
            userChallenge.getChallenge().addFailedPoint(userChallenge.getChallenge().getDepositPoint());
        }
    }
}
