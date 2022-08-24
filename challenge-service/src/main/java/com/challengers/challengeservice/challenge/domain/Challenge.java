package com.challengers.challengeservice.challenge.domain;

import com.challengers.challengeservice.common.BaseTimeEntity;
import com.challengers.challengeservice.challenge.dto.ChallengeRequest;
import com.challengers.challengeservice.examplephoto.domain.ExamplePhoto;
import com.challengers.challengeservice.tag.domain.ChallengeTags;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseTimeEntity {
    @Setter @Id @GeneratedValue
    @Column(name = "challenge_id")
    private Long id;

    private Long hostId;

    private String name;
    private String imageUrl;
    private String photoDescription;
    private String challengeRule;
    @Enumerated(EnumType.STRING)
    private CheckFrequencyType checkFrequencyType;
    private int checkTimesPerRound;
    @Enumerated(EnumType.STRING)
    private Category category;
    private LocalDate startDate;
    private LocalDate endDate;
    private int depositPoint;
    private String introduction;
    private Float totalStarRating;
    private Float starRating;
    private int reviewCount;
    private int userCount;
    private int userCountLimit;
    private int failedPoint;
    private int round;
    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    @Embedded
    private ChallengeTags challengeTags = ChallengeTags.empty();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ExamplePhoto> examplePhotos = new ArrayList<>();


    public void addExamplePhotos(List<String> examplePhotoUrls) {
        for (String url : examplePhotoUrls) {
            ExamplePhoto examplePhoto = new ExamplePhoto(this, url);
            examplePhotos.add(examplePhoto);
            examplePhoto.setChallenge(this);
        }
    }

    public List<String> getExamplePhotoUrls() {
        return examplePhotos.stream()
                .map(ExamplePhoto::getPhoto_url)
                .collect(Collectors.toList());
    }

    @Builder
    public Challenge(Long id, Long hostId, String name, String imageUrl, String photoDescription,
                     String challengeRule, CheckFrequencyType checkFrequencyType, int checkTimesPerRound, Category category,
                     LocalDate startDate, LocalDate endDate, int depositPoint, String introduction,
                     Float totalStarRating, Float starRating, int reviewCount, int userCount,
                     int userCountLimit, int failedPoint, int round, ChallengeStatus status,
                     LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.hostId = hostId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.photoDescription = photoDescription;
        this.challengeRule = challengeRule;
        this.checkFrequencyType = checkFrequencyType;
        this.checkTimesPerRound = checkTimesPerRound;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositPoint = depositPoint;
        this.introduction = introduction;
        this.totalStarRating = totalStarRating;
        this.starRating = starRating;
        this.reviewCount = reviewCount;
        this.userCount = userCount;
        this.userCountLimit = userCountLimit;
        this.failedPoint = failedPoint;
        this.round = round;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public static Challenge create(ChallengeRequest request, Long host, String imageUrl, List<String> examplePhotoUrls) {
        validate(request);
        Challenge challenge = request.toChallenge();
        challenge.setHostId(host);
        challenge.setImageUrl(imageUrl);
        challenge.addExamplePhotos(examplePhotoUrls);
        challenge.initStatus();
        return challenge;
    }

    private static void validate(ChallengeRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        if (startDate.isEqual(endDate) || startDate.isAfter(endDate))
            throw new RuntimeException("챌린지 종료일은 챌린지 시작일 이후이여야 합니다.");
    }

    public void update(String imageUrl, String introduction) {
        this.imageUrl = imageUrl;
        this.introduction = introduction;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void joinUser() {
        this.userCount++;
    }

    public void addReviewRelation(Float starRating) {
        reviewCount++;
        totalStarRating += starRating;
        updateStarRating();
    }

    public void deleteReviewRelation(Float starRating) {
        reviewCount--;
        totalStarRating -= starRating;
        updateStarRating();
    }

    public void updateReviewRelation(Float starRating, Float newStarRating) {
        totalStarRating = totalStarRating - starRating + newStarRating;
        updateStarRating();
    }

    public void toInProgress() {
        status = ChallengeStatus.IN_PROGRESS;
    }

    public void toValidate() {
        this.status = ChallengeStatus.VALIDATE;
    }

    public void toFinish() {
        this.status = ChallengeStatus.FINISH;
    }

    private void updateStarRating() {
        starRating = reviewCount == 0 ? 0.0f : Math.round(totalStarRating/reviewCount*10)/10.0f;
    }

    public void initStatus() {
        if (startDate.isAfter(LocalDate.now())) status = ChallengeStatus.READY;
        else {
            status = ChallengeStatus.IN_PROGRESS;
            round = 1;
        }
    }

    public void updateRound() {
        round++;
    }

    public void addFailedPoint(long point) {
        failedPoint += point;
    }
}
