package com.challengers.challengeservice.photocheck.service;

import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.domain.ChallengeStatus;
import com.challengers.challengeservice.challenge.repository.ChallengeRepository;
import com.challengers.challengeservice.challengephoto.domain.ChallengePhoto;
import com.challengers.challengeservice.challengephoto.repository.ChallengePhotoRepository;
import com.challengers.challengeservice.common.AwsS3Uploader;
import com.challengers.challengeservice.photocheck.domain.PhotoCheck;
import com.challengers.challengeservice.photocheck.domain.PhotoCheckStatus;
import com.challengers.challengeservice.photocheck.dto.CheckRequest;
import com.challengers.challengeservice.photocheck.dto.PhotoCheckRequest;
import com.challengers.challengeservice.photocheck.dto.PhotoCheckResponse;
import com.challengers.challengeservice.photocheck.repository.PhotoCheckRepository;
import com.challengers.challengeservice.userchallenge.domain.UserChallenge;
import com.challengers.challengeservice.userchallenge.domain.UserChallengeStatus;
import com.challengers.challengeservice.userchallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PhotoCheckService {
    private final AwsS3Uploader awsS3Uploader;
    private final ChallengeRepository challengeRepository;
    private final ChallengePhotoRepository challengePhotoRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final PhotoCheckRepository photoCheckRepository;

    @Transactional(readOnly = true)
    public PhotoCheckResponse findPhotoCheck(Long photoCheckId) {
        return PhotoCheckResponse.of(photoCheckRepository
                .findById(photoCheckId).orElseThrow(NoSuchElementException::new));
    }

    @Transactional
    public Long addPhotoCheck(PhotoCheckRequest photoCheckRequest, Long userId) {
        Challenge challenge = challengeRepository.findById(photoCheckRequest.getChallengeId()).orElseThrow(NoSuchElementException::new);
        if (!challenge.getStatus().equals(ChallengeStatus.IN_PROGRESS))
            throw new RuntimeException("진행중인 챌린지가 아닙니다.");

        UserChallenge userChallenge = userChallengeRepository.findByUserIdAndChallengeId(userId, challenge.getId())
                .orElseThrow(NoSuchElementException::new);

        if(!userChallenge.getStatus().equals(UserChallengeStatus.IN_PROGRESS))
            throw new RuntimeException("해당 챌린지에 참여중이 아닙니다.");

        if (photoCheckRepository.countByUserChallengeIdAndRound(challenge.getId(), challenge.getRound())
                >= challenge.getCheckTimesPerRound())
            throw new RuntimeException("이미 해당 회차에 인증 사진을 전부 올렸습니다.");

        String photoUrl = awsS3Uploader.uploadImage(photoCheckRequest.getPhoto());
        ChallengePhoto challengePhoto = ChallengePhoto.builder()
                .challenge(challenge)
                .userId(userId)
                .photoUrl(photoUrl)
                .build();
        challengePhotoRepository.save(challengePhoto);

        PhotoCheck photoCheck = PhotoCheck.builder()
                .userChallenge(userChallenge)
                .challengePhoto(challengePhoto)
                .round(challenge.getRound())
                .status(PhotoCheckStatus.WAITING)
                .build();
        photoCheckRepository.save(photoCheck);

        return photoCheck.getId();
    }

    // TODO : Bulk Update 적용하기
    @Transactional
    public void passPhotoCheck(CheckRequest checkRequest, Long userId) {
        Long hostId = photoCheckRepository.findById(checkRequest.getPhotoCheckIds().get(0))
                .orElseThrow(NoSuchElementException::new)
                .getUserChallenge().getChallenge().getHostId();
        if (!hostId.equals(userId))
            throw new RuntimeException("인증샷을 처리할 권한이 없습니다.");

        for (Long photoCheckId : checkRequest.getPhotoCheckIds()) {
            PhotoCheck photoCheck = photoCheckRepository.findById(photoCheckId)
                    .orElseThrow(NoSuchElementException::new);
            if (photoCheck.getStatus().equals(PhotoCheckStatus.PASS))
                throw new RuntimeException("이미 인증 통과된 사진이 있습니다.");
            photoCheck.pass();
        }
    }

    // TODO : Bulk Update 적용하기
    @Transactional
    public void failPhotoCheck(CheckRequest checkRequest, Long userId) {
        Long hostId = photoCheckRepository.findById(checkRequest.getPhotoCheckIds().get(0))
                .orElseThrow(NoSuchElementException::new)
                .getUserChallenge().getChallenge().getHostId();
        if (!hostId.equals(userId))
            throw new RuntimeException("인증샷을 처리할 권한이 없습니다.");

        for (Long photoCheckId : checkRequest.getPhotoCheckIds()) {
            PhotoCheck photoCheck = photoCheckRepository.findById(photoCheckId)
                    .orElseThrow(NoSuchElementException::new);
            if (photoCheck.getStatus().equals(PhotoCheckStatus.FAIL))
                throw new RuntimeException("이미 인증 실패 처리된 사진이 있습니다.");
            photoCheck.fail();
        }
    }

}
