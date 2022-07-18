package com.challengers.challengeservice.challengephoto.controller;

import com.challengers.challengeservice.challengephoto.dto.ChallengePhotoRequest;
import com.challengers.challengeservice.challengephoto.service.ChallengePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/challenge_photo")
@RequiredArgsConstructor
public class ChallengePhotoController {
    private final ChallengePhotoService challengePhotoService;

    @PostMapping("/{challenge_id}")
    public ResponseEntity<Void> addChallengePhoto(@ModelAttribute ChallengePhotoRequest challengePhotoRequest,
                                                  @PathVariable(name = "challenge_id") Long challengeId,
                                                  @RequestHeader(value = "userId") String userId) {
        challengePhotoService.upload(challengePhotoRequest, challengeId, Long.parseLong(userId));

        return ResponseEntity.ok().build();

    }
}
