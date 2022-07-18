package com.challengers.challengeservice.cart.controller;

import com.challengers.challengeservice.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/{challenge_id}")
    public ResponseEntity<Void> addCart(@PathVariable(name = "challenge_id") Long challengeId,
                                        @RequestHeader(value = "userId") String userId) {
        cartService.put(challengeId, Long.parseLong(userId));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{challenge_id}")
    public ResponseEntity<Void> deleteCart(@PathVariable(name = "challenge_id") Long challengeId,
                                           @RequestHeader(value = "userId") String userId) {

        cartService.takeOut(challengeId, Long.parseLong(userId));

        return ResponseEntity.ok().build();
    }

}
