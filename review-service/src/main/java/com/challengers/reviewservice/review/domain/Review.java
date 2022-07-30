package com.challengers.reviewservice.review.domain;

import com.challengers.reviewservice.review.common.BaseTimeEntity;
import com.challengers.reviewservice.review.dto.ReviewUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "uid")
    private Long userId;
    @Column(name = "cid")
    private Long challengeId;
    private String title;
    private String content;
    private Float starRating;

    @Builder
    public Review(Long id, Long userId, Long challengeId, String title, String content, Float starRating) {
        this.id = id;
        this.userId = userId;
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.starRating = starRating;
    }

    public void update(ReviewUpdateRequest reviewUpdateRequest) {
//        challengeId.updateReviewRelation(starRating, reviewUpdateRequest.getStarRating());

        title = reviewUpdateRequest.getTitle();
        content = reviewUpdateRequest.getContent();
        starRating = reviewUpdateRequest.getStarRating();
    }
}
