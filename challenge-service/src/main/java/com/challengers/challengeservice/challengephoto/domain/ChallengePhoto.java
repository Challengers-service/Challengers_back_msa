package com.challengers.challengeservice.challengephoto.domain;

import com.challengers.challengeservice.challenge.domain.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChallengePhoto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private Long userId;

    private String photoUrl;

    @Builder
    public ChallengePhoto(Long id, Challenge challenge, Long userId, String photoUrl) {
        this.id = id;
        this.challenge = challenge;
        this.userId = userId;
        this.photoUrl = photoUrl;
    }

    public static ChallengePhoto create(Challenge challenge, Long userId, String photoUrl) {
        return ChallengePhoto.builder()
                .challenge(challenge)
                .userId(userId)
                .photoUrl(photoUrl)
                .build();
    }
}
