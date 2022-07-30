package com.challengers.reviewservice.review.dto;

import com.challengers.reviewservice.review.common.UserDto;
import com.challengers.reviewservice.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewResponse {
    private Long reviewId;
    private String title;
    private String content;
    private Float starRating;
    private String createdDate;

    private Long userId;
    private String userName;
    private String profileImageUrl;

    public static ReviewResponse of(Review review, UserDto userDto) {
        return new ReviewResponse(review.getId(), review.getTitle(), review.getContent(), review.getStarRating(),
                review.getCreatedDateYYYYMMDD(), review.getUserId(), userDto.getName(), userDto.getImageUrl());
    }
}
