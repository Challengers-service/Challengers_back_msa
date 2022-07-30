package com.challengers.reviewservice.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequest {
    String title;
    String content;
    float starRating;
}
