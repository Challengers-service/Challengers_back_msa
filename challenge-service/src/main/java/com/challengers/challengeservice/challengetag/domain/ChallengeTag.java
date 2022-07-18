package com.challengers.challengeservice.challengetag.domain;

import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.tag.domain.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public static ChallengeTag associate(Challenge challenge, Tag tag) {
        ChallengeTag challengeTag = new ChallengeTag();
        challengeTag.setTag(tag);
        challengeTag.setChallenge(challenge);

        return challengeTag;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
        challenge.getChallengeTags().addChallengeTag(this);
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
