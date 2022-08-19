package com.challengers.challengeservice.photocheck.repository;


import com.challengers.challengeservice.photocheck.domain.PhotoCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoCheckRepository extends JpaRepository<PhotoCheck,Long> {
    Long countByUserChallengeIdAndRound(Long challengeId, Integer round);

    List<PhotoCheck> findByUserChallengeId(Long userChallengeId);
}
