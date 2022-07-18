package com.challengers.challengeservice.cart.service;

import com.challengers.challengeservice.cart.domain.Cart;
import com.challengers.challengeservice.cart.repository.CartRepository;
import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ChallengeRepository challengeRepository;

    public void put(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(NoSuchElementException::new);

        cartRepository.save(Cart.create(challenge,userId));
    }


    public void takeOut(Long challengeId, Long userId) {
        Cart cart = cartRepository.findByChallengeIdAndUserId(challengeId,userId).orElseThrow(NoSuchElementException::new);
        if (!cart.getUserId().equals(userId))
            throw new RuntimeException("찜하기를 취소할 권한이 없습니다.");

        cartRepository.delete(cart);
    }
}
