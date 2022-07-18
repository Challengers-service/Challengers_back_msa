package com.challengers.challengeservice.challengephoto.repository;

import com.challengers.challengeservice.challengephoto.domain.ChallengePhoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChallengePhotoRepository extends JpaRepository<ChallengePhoto,Long> {
    Page<ChallengePhoto> findAllByUserId(Pageable pageable, Long userId);
}
