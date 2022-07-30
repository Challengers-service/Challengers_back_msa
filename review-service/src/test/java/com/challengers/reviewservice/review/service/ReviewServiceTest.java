package com.challengers.reviewservice.review.service;

import com.challengers.reviewservice.review.client.UserClient;
import com.challengers.reviewservice.review.domain.Review;
import com.challengers.reviewservice.review.dto.ReviewRequest;
import com.challengers.reviewservice.review.dto.ReviewUpdateRequest;
import com.challengers.reviewservice.review.repository.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock ReviewRepository reviewRepository;
    @Mock UserClient userClient;
    ReviewService reviewService;

    Review review;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewService(reviewRepository, userClient);

        review = Review.builder()
                .id(1L)
                .userId(1L)
                .challengeId(1L)
                .title("리뷰 제목입니다.")
                .content("리뷰 내용입니다.")
                .starRating(5.0f)
                .build();
    }

    @Test
    @DisplayName("리뷰 생성 성공")
    void create() {
        ReviewRequest reviewRequest = new ReviewRequest(review.getChallengeId(),"title",
                "content",3.4f);
        when(reviewRepository.findByChallengeIdAndUserId(any(),any()))
                .thenReturn(Optional.empty());

        reviewService.create(reviewRequest,1L);

        verify(reviewRepository).save(any());
    }

    @Test
    @DisplayName("리뷰 생성 실패 - 이미 작성한 리뷰가 있는 경우")
    void create_failed_because_there_are_other_reviews_already_written() {
        ReviewRequest reviewRequest = new ReviewRequest(review.getChallengeId(),"title",
                "content",3.4f);
        when(reviewRepository.findByChallengeIdAndUserId(any(),any())).thenReturn(Optional.of(review));

        assertThatThrownBy(() -> reviewService.create(reviewRequest,1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void delete() {
        when(reviewRepository.findById(any())).thenReturn(Optional.of(review));

        reviewService.delete(1L,review.getUserId());

        verify(reviewRepository).delete(any());
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    void update() {
        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest("수정할 리뷰 제목입니다.",
                "수정할 리뷰 내용입니다.", 3.2f);
        when(reviewRepository.findById(any())).thenReturn(Optional.of(review));

        reviewService.update(1L,reviewUpdateRequest,review.getUserId());

        assertThat(review.getTitle()).isEqualTo(reviewUpdateRequest.getTitle());
        assertThat(review.getContent()).isEqualTo(reviewUpdateRequest.getContent());
        assertThat(review.getStarRating()).isEqualTo(reviewUpdateRequest.getStarRating());
    }
}