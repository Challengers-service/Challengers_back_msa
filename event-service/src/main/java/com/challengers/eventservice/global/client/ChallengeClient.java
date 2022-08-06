package com.challengers.eventservice.global.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "challenge-service")
public interface ChallengeClient {

    @PostMapping("/do-challenge-update-event")
    void doChallengeUpdateEvent();
}
