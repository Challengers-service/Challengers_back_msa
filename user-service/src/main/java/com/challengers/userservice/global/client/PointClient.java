package com.challengers.userservice.global.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "point-service")
public interface PointClient {

    @PostMapping("/api/point")
    void createPointInfo(@RequestHeader(name = "userId") String userId);
}
