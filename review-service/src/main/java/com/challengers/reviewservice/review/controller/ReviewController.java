package com.challengers.reviewservice.review.controller;

import com.challengers.reviewservice.review.dto.ReviewRequest;
import com.challengers.reviewservice.review.dto.ReviewResponse;
import com.challengers.reviewservice.review.dto.ReviewUpdateRequest;
import com.challengers.reviewservice.review.repository.ReviewRepository;
import com.challengers.reviewservice.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    private final ReviewRepository reviewRepository;

    @GetMapping("/{challengeId}")
    public ResponseEntity<Page<ReviewResponse>> showReviews(@PageableDefault(size = 6) Pageable pageable,
                                                            @PathVariable(name = "challengeId") Long challengeId) {
        return ResponseEntity.ok(reviewService.findReviews(pageable, challengeId));
    }

    @PostMapping
    public ResponseEntity<Void> addReview(@Valid @RequestBody ReviewRequest reviewRequest,
                                          @RequestHeader(value = "userId") Long userId) {
        reviewService.create(reviewRequest, userId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable(name = "reviewId") Long reviewId,
                                             @RequestHeader(value = "userId") Long userId) {
        reviewService.delete(reviewId, userId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable(name = "reviewId") Long reviewId,
                                          @RequestBody ReviewUpdateRequest reviewUpdateRequest,
                                             @RequestHeader(value = "userId") Long userId) {
        reviewService.update(reviewId, reviewUpdateRequest, userId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/count/{challengeId}")
    public ResponseEntity<Integer> getReviewCount(@PathVariable(name = "challengeId") Long challengeId) {
        return ResponseEntity.ok(reviewRepository.countByChallengeId(challengeId));
    }

    @GetMapping("/star_rating/{challengeId}")
    public ResponseEntity<Float> getStarRatingAvg(@PathVariable(name = "challengeId") Long challengeId) {
        return ResponseEntity.ok(reviewRepository.getStarRatingAvgByChallengeId(challengeId));
    }
}
