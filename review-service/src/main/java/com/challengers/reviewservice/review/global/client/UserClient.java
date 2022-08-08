package com.challengers.reviewservice.review.global.client;

import com.challengers.reviewservice.review.global.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/user/global/{userId}")
    UserDto getUserInfo(@PathVariable String userId);
}
