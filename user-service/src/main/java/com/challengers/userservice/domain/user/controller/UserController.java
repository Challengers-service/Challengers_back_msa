package com.challengers.userservice.domain.user.controller;

import com.challengers.userservice.domain.user.dto.UserMeResponse;
import com.challengers.userservice.domain.user.dto.UserUpdateRequest;
import com.challengers.userservice.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getCurrentUser(@Valid @RequestHeader(value="userId") String userId) {
        return ResponseEntity.ok(userService.getCurrentUser(Long.parseLong(userId)));
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateUser(@Valid @RequestHeader(value="userId") String userId, @Valid @ModelAttribute UserUpdateRequest userUpdateRequest){
        userService.updateUser(Long.parseLong(userId), userUpdateRequest);
        return ResponseEntity.ok().build();
    }
}