package com.challengers.reviewservice.review.service;


import com.challengers.reviewservice.review.global.client.UserClient;
import com.challengers.reviewservice.review.global.dto.UserDto;
import com.challengers.reviewservice.review.domain.Review;
import com.challengers.reviewservice.review.dto.ReviewRequest;
import com.challengers.reviewservice.review.dto.ReviewResponse;
import com.challengers.reviewservice.review.dto.ReviewUpdateRequest;
import com.challengers.reviewservice.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserClient userClient;

    private final CircuitBreakerFactory circuitBreakerFactory;

    @Transactional
    public void create(ReviewRequest reviewRequest, Long userId) {
        if (reviewRepository.findByChallengeIdAndUserId(reviewRequest.getChallengeId(), userId).isPresent())
            throw new RuntimeException("한 챌린지에 하나의 리뷰만 생성할 수 있습니다.");

        Review review = Review.builder()
                .challengeId(reviewRequest.getChallengeId())
                .userId(userId)
                .title(reviewRequest.getTitle())
                .content(reviewRequest.getContent())
                .starRating(reviewRequest.getStarRating())
                .build();

        reviewRepository.save(review);
    }

    @Transactional
    public void delete(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NoSuchElementException::new);
        authorization(review.getUserId(), userId);

        reviewRepository.delete(review);
    }

    @Transactional
    public void delete(Long challengeId) {
        reviewRepository.deleteByChallengeId(challengeId);
    }

    @Transactional
    public void update(Long reviewId, ReviewUpdateRequest reviewUpdateRequest, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NoSuchElementException::new);
        authorization(review.getUserId(), userId);
        review.update(reviewUpdateRequest);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> findReviews(Pageable pageable, Long challengeId) {
        return reviewRepository.findAllByChallengeId(pageable, challengeId)
                .map(review -> {
                    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("getUserInfo");
                    return ReviewResponse.of(review,
                            circuitBreaker.run(() -> userClient.getUserInfo(String.valueOf(review.getUserId())),
                            throwable -> new UserDto(null, null, null)));
                });
    }

    private void authorization(Long authorizationId, Long userId) {
        if (!authorizationId.equals(userId)) throw new RuntimeException("권한이 없습니다.");
    }
}
