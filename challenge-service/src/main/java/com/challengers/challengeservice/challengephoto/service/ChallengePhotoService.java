package com.challengers.challengeservice.challengephoto.service;

import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.repository.ChallengeRepository;
import com.challengers.challengeservice.challengephoto.domain.ChallengePhoto;
import com.challengers.challengeservice.challengephoto.dto.ChallengePhotoRequest;
import com.challengers.challengeservice.challengephoto.repository.ChallengePhotoRepository;
import com.challengers.challengeservice.common.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChallengePhotoService {
    private final ChallengePhotoRepository challengePhotoRepository;
    private final ChallengeRepository challengeRepository;
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public void upload(ChallengePhotoRequest challengePhotoRequest, Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);
        String photoUrl = awsS3Uploader.uploadImage(challengePhotoRequest.getPhoto());
        challengePhotoRepository.save(ChallengePhoto.create(challenge,userId,photoUrl));
    }
}
