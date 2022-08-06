package com.challengers.challengeservice.userchallenge.domain;


import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.photocheck.domain.PhotoCheck;
import com.challengers.challengeservice.userchallenge.ChallengeJoinManager;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChallenge {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_challenge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private Long userId;

    @OneToMany(mappedBy = "userChallenge")
    private List<PhotoCheck> photoChecks = new ArrayList<>();

    private int maxProgress;
    private int progress;
    @Enumerated(EnumType.STRING)
    private UserChallengeStatus status;

    @Builder
    public UserChallenge(Long id, Challenge challenge, Long userId, int maxProgress, int progress, UserChallengeStatus status) {
        this.id = id;
        this.challenge = challenge;
        this.maxProgress = maxProgress;
        this.progress = progress;
        this.userId = userId;
        this.status = status;
    }

    public static UserChallenge create(Challenge challenge, Long userId) {
        return UserChallenge.builder()
                .challenge(challenge)
                .userId(userId)
                .maxProgress(ChallengeJoinManager.getMaxProgress(challenge))
                .progress(0)
                .status(UserChallengeStatus.IN_PROGRESS)
                .build();
    }

    public void fail() {
        status = UserChallengeStatus.FAIL;
    }
}
