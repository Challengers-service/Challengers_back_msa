package com.challengers.eventservice;


import com.challengers.eventservice.global.client.ChallengeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class EventScheduler {
    private ChallengeClient challengeClient;

    @Scheduled(cron = "0 0 0 * * ?")
    public void everyday() {
        challengeClient.doChallengeUpdateEvent();
    }
}
