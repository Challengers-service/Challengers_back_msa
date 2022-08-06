package com.challengers.challengeservice.challengeEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChallengeEventController {
    private final ChallengeEventService challengeEventService;

    @PostMapping("/do-challenge-update-event")
    public void doChallengeUpdateEvent() {
        challengeEventService.challengeUpdateEvent();
    }
}
