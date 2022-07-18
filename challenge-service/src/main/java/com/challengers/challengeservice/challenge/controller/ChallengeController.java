package com.challengers.challengeservice.challenge.controller;


import com.challengers.challengeservice.challenge.dto.ChallengeDetailResponse;
import com.challengers.challengeservice.challenge.dto.ChallengeRequest;
import com.challengers.challengeservice.challenge.dto.ChallengeResponse;
import com.challengers.challengeservice.challenge.dto.ChallengeUpdateRequest;
import com.challengers.challengeservice.challenge.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
                                                                 @RequestHeader(required = false, value = "userId") String userId) {
        return ResponseEntity.ok(challengeService.findChallenge(id,userId==null?null:Long.parseLong(userId)));
    }

    @GetMapping
    public ResponseEntity<Page<ChallengeResponse>> findCanJoinChallenges(@PageableDefault(size = 6) Pageable pageable,
                                                                         @RequestHeader(required = false, value = "userId") String userId) {

        return ResponseEntity.ok(challengeService.findReadyOrInProgressChallenges(pageable, userId==null?null:Long.parseLong(userId)));
    }

    @PostMapping
    public ResponseEntity<Void> createChallenge(@Valid @ModelAttribute ChallengeRequest challengeRequest,
                                                @RequestHeader(value = "userId") String userId) {
        Long challengeId = challengeService.create(challengeRequest, Long.parseLong(userId));
        return ResponseEntity.created(URI.create("/api/challenge/"+challengeId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateChallenge(@Valid @ModelAttribute ChallengeUpdateRequest challengeUpdateRequest,
                                                @PathVariable(name = "id") Long challengeId,
                                                @RequestHeader(value = "userId") String userId) {

        challengeService.update(challengeUpdateRequest, challengeId, Long.parseLong(userId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id,
                                                @RequestHeader(value = "userId") String userId) {
        challengeService.delete(id, Long.parseLong(userId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<Void> joinChallenge(@PathVariable Long id,
                                              @RequestHeader(value = "userId") String userId) {
        challengeService.join(id, Long.parseLong(userId));

        return ResponseEntity.ok().build();
    }
}
