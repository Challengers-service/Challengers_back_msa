package com.challengers.challengeservice.challenge.controller;


import com.challengers.challengeservice.challenge.dto.*;
import com.challengers.challengeservice.challenge.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDetailResponse> findChallenge(@PathVariable Long id,
                                                                 @RequestHeader(required = false, value = "userId") Long userId) {

        return ResponseEntity.ok(challengeService.findChallenge(id,userId));
    }

    @GetMapping
    public ResponseEntity<Page<ChallengeResponse>> search(Pageable pageable,
                                                  ChallengeSearchCondition condition,
                                                  @RequestHeader(required = false, value = "userId") Long userId) {

        return ResponseEntity.ok(challengeService.search(condition, pageable, userId));
    }

    @PostMapping
    public ResponseEntity<Void> createChallenge(@Valid @ModelAttribute ChallengeRequest challengeRequest,
                                                @RequestHeader(value = "userId") Long userId) {

        Long challengeId = challengeService.create(challengeRequest, userId);
        return ResponseEntity.created(URI.create("/api/challenge/"+challengeId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateChallenge(@Valid @ModelAttribute ChallengeUpdateRequest challengeUpdateRequest,
                                                @PathVariable(name = "id") Long challengeId,
                                                @RequestHeader(value = "userId") Long userId) {

        challengeService.update(challengeUpdateRequest, challengeId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id,
                                                @RequestHeader(value = "userId") Long userId) {

        challengeService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<Void> joinChallenge(@PathVariable Long id,
                                              @RequestHeader(value = "userId") Long userId) {

        challengeService.join(id, userId);
        return ResponseEntity.ok().build();
    }
}
