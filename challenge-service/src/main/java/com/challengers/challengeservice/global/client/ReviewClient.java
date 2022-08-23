package com.challengers.challengeservice.global.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "review-service")
public interface ReviewClient {

    @GetMapping("/api/reviews/count/{challengeId}")
    Integer getReviewCount(@PathVariable(name = "challengeId") Long challengeId);

    @GetMapping("/api/reviews/star_rating/{challengeId}")
    Float getStarRatingAvg(@PathVariable(name = "challengeId") Long challengeId);

    @DeleteMapping("/api/reviews")
    void deleteReview(@RequestParam(name = "challengeId") Long challengeId);
}
