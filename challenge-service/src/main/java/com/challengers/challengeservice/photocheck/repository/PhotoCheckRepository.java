package com.challengers.challengeservice.photocheck.repository;


import com.challengers.challengeservice.photocheck.domain.PhotoCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoCheckRepository extends JpaRepository<PhotoCheck,Long> {
    Long countByUserChallengeIdAndRound(Long challengeId, Integer round);
}
