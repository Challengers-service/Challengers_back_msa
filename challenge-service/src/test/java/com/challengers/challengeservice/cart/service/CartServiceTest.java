package com.challengers.challengeservice.cart.service;

import com.challengers.challengeservice.cart.domain.Cart;
import com.challengers.challengeservice.cart.repository.CartRepository;
import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.repository.ChallengeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock CartRepository cartRepository;
    @Mock ChallengeRepository challengeRepository;

    CartService cartService;
    Challenge challenge;
    Cart cart;

    @BeforeEach
    void setUp() {
        cartService = new CartService(cartRepository,
                challengeRepository);

        challenge = Challenge.builder()
                .hostId(1L)
                .build();

        cart = Cart.create(challenge,1L);
    }

    @Test
    @DisplayName("찜하기 성공")
    void put() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));

        cartService.put(1L,1L);

        verify(cartRepository).save(any());
    }

    @Test
    @DisplayName("찜하기 취소 성공")
    void takeOut() {
        when(cartRepository.findByChallengeIdAndUserId(any(),any())).thenReturn(Optional.of(cart));

        cartService.takeOut(1L,1L);

        verify(cartRepository).delete(any());
    }

    @Test
    @DisplayName("찜하기 취소 실패 - 취소 권한이 없다.")
    void takeOut_fail_unauthorized() {
        when(cartRepository.findByChallengeIdAndUserId(any(),any())).thenReturn(Optional.of(cart));

        Assertions.assertThatThrownBy(()->cartService.takeOut(1L,2L))
                .isInstanceOf(RuntimeException.class);
    }
}