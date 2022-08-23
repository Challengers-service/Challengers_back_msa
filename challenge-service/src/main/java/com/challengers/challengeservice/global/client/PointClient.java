package com.challengers.challengeservice.global.client;

import com.challengers.challengeservice.global.dto.GiveRewardDto;
import com.challengers.challengeservice.global.dto.PointUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "point-service")
public interface PointClient {

    @PostMapping("/api/point/global/give_reward")
    void giveReward(@RequestBody GiveRewardDto giveRewardDto);

    @PutMapping("/api/point/global")
    void updateMyPoint(@RequestHeader(name = "userId") Long userId,
                       @RequestBody PointUpdateRequest pointUpdateRequest);
}
