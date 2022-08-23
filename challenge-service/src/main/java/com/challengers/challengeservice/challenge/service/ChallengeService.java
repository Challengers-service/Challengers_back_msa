package com.challengers.challengeservice.challenge.service;


import com.challengers.challengeservice.cart.repository.CartRepository;
import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.dto.*;
import com.challengers.challengeservice.challenge.repository.ChallengeRepository;
import com.challengers.challengeservice.challengephoto.repository.ChallengePhotoRepository;
import com.challengers.challengeservice.challengetag.domain.ChallengeTag;
import com.challengers.challengeservice.common.AwsS3Uploader;
import com.challengers.challengeservice.global.client.PointClient;
import com.challengers.challengeservice.global.client.ReviewClient;
import com.challengers.challengeservice.global.dto.PointUpdateRequest;
import com.challengers.challengeservice.photocheck.repository.PhotoCheckRepository;
import com.challengers.challengeservice.tag.domain.Tag;
import com.challengers.challengeservice.tag.repository.TagRepository;
import com.challengers.challengeservice.userchallenge.ChallengeJoinManager;
import com.challengers.challengeservice.userchallenge.domain.UserChallenge;
import com.challengers.challengeservice.userchallenge.domain.UserChallengeStatus;
import com.challengers.challengeservice.userchallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final TagRepository tagRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final CartRepository cartRepository;
    private final PhotoCheckRepository photoCheckRepository;
    private final ChallengePhotoRepository challengePhotoRepository;
    private final PointClient pointClient;
    private final ReviewClient reviewClient;

    @Transactional
    public Long create(ChallengeRequest challengeRequest, Long userId) {
        // challenge 시작일, 종료일이 올바르지 않을 경우 에러 반환시켜야함

        String imageUrl = "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/challengeDefaultImage.jpg";
        if (challengeRequest.getImage() != null)
            imageUrl = awsS3Uploader.uploadImage(challengeRequest.getImage());
        List<String> examplePhotoUrls = awsS3Uploader.uploadImages(challengeRequest.getExamplePhotos());

        Challenge challenge = Challenge.create(challengeRequest, userId, imageUrl, examplePhotoUrls);
        challengeRepository.save(challenge);

        challengeRequest.getTags()
                .forEach(tag -> ChallengeTag.associate(challenge,findOrCreateTag(tag)));

        userChallengeRepository.save(UserChallenge.create(challenge,userId));

        pointClient.updateMyPoint(userId, new PointUpdateRequest(challengeRequest.getDepositPoint() * -1L,"DEPOSIT"));
        
        /*
        host.update(host.getChallengeCount() + 1);

        updateChallengeAchievement(host);

         */

        return challenge.getId();
    }

    @Transactional
    public void update(ChallengeUpdateRequest challengeUpdateRequest, Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        if (!challenge.getHostId().equals(userId)) throw new RuntimeException("권한이 없는 요청");

        String imageUrl = challenge.getImageUrl();
        if (challengeUpdateRequest.getImage()!=null) {
            awsS3Uploader.deleteImage(challenge.getImageUrl());
            imageUrl = awsS3Uploader.uploadImage(challengeUpdateRequest.getImage());
        }

        challenge.update(imageUrl,challengeUpdateRequest.getIntroduction());
    }

    @Transactional
    public void delete(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        if (!challenge.getHostId().equals(userId)) throw new RuntimeException("권한이 없는 요청");
        if (userChallengeRepository.countByChallengeId(challengeId) != 1) throw new RuntimeException("삭제 조건에 부합하지 않음 - 챌린지 참여자가 2명 이상 있음");

        awsS3Uploader.deleteImage(challenge.getImageUrl());
        awsS3Uploader.deleteImages(challenge.getExamplePhotoUrls());
        UserChallenge userChallenge = userChallengeRepository.findByUserIdAndChallengeId(challenge.getHostId(), challengeId).orElseThrow(NoSuchElementException::new);
        photoCheckRepository.findByUserChallengeId(userChallenge.getId()).forEach(photoCheck -> {
            photoCheckRepository.delete(photoCheck);
            challengePhotoRepository.delete(photoCheck.getChallengePhoto());
        });

        userChallengeRepository.delete(userChallenge);

        cartRepository.findByChallengeId(challengeId).forEach(cartRepository::delete);

        pointClient.updateMyPoint(userId, new PointUpdateRequest((long)challenge.getDepositPoint(),"CANCEL"));

        challengeRepository.delete(challenge);
    }

    @Transactional(readOnly = true)
    public ChallengeDetailResponse findChallenge(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);

        List<UserChallenge> userChallenges = userChallengeRepository
                .findByChallengeIdAndStatus(challengeId, UserChallengeStatus.IN_PROGRESS);
        long progress = 0L;

        for (UserChallenge userChallenge : userChallenges) {
            progress += userChallenge.getMaxProgress();
        }
        int maxProgress = ChallengeJoinManager.getMaxProgress(challenge);


        return ChallengeDetailResponse.of(challenge,
                userChallengeRepository.countByChallengeId(challengeId),
                reviewClient.getStarRatingAvg(challengeId),
                reviewClient.getReviewCount(challengeId),
                cartRepository.findByChallengeIdAndUserId(challengeId, userId).isPresent(),
                challenge.getFailedPoint()/(progress+maxProgress)*maxProgress);
    }

    @Transactional
    public void join(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        if (userChallengeRepository.countByChallengeId(challengeId) == challenge.getUserCountLimit())
            throw new RuntimeException("참여 인원이 가득 찼습니다.");

        if (userChallengeRepository.findByUserIdAndChallengeId(userId,challengeId).isPresent())
            throw new RuntimeException("이미 참여하고 있는 챌린지 입니다.");

        if (!ChallengeJoinManager.canJoin(challenge))
            throw new RuntimeException(
                    "다음주 월요일까지 남은 일 수 보다 일주일에 인증해야 하는 횟수가 많기때문에 다음 주에 참여해야 합니다.");
        /*
        updateChallengeAchievement(user);

         */
        pointClient.updateMyPoint(userId, new PointUpdateRequest(challenge.getDepositPoint()*-1L,"DEPOSIT"));

        userChallengeRepository.save(UserChallenge.create(challenge, userId));
    }

    @Transactional(readOnly = true)
    public Page<ChallengeResponse> search(ChallengeSearchCondition condition, Pageable pageable, Long userId) {
        return challengeRepository.search(condition, pageable).map(challenge -> new ChallengeResponse(challenge,
                userId != null && cartRepository.findByChallengeIdAndUserId(challenge.getId(), userId).isPresent(),
                userChallengeRepository.findByChallengeId(challenge.getId())
                        .stream().map(UserChallenge::getUserId)
                        .collect(Collectors.toList())
        ));
    }

    /*
    private void updateChallengeAchievement(User user){
        if(user.getChallengeCount() == 1){
            Achievement achievement = Achievement.builder()
                    .user(user)
                    .award(Award.ONE_PARTICIPATION)
                    .build();

            achievementRepository.save(achievement);
        } else if(user.getChallengeCount() == 50){
            Achievement achievement = Achievement.builder()
                    .user(user)
                    .award(Award.FIFTY_PARTICIPATION)
                    .build();

            achievementRepository.save(achievement);
        }
    }

     */

    private Tag findOrCreateTag(String tag) {
        Tag findTag = tagRepository.findTagByName(tag).orElse(null);
        if (findTag == null) {
            findTag = new Tag(tag);
            tagRepository.save(findTag);
        }
        return findTag;
    }
}
