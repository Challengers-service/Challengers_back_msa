package com.challengers.challengeservice.cart.domain;

import com.challengers.challengeservice.challenge.domain.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private Long userId;

    @Builder
    public Cart(Long id, Challenge challenge, Long userId) {
        this.id = id;
        this.challenge = challenge;
        this.userId = userId;
    }

    public static Cart create(Challenge challenge, Long userId) {
        return Cart.builder()
                .challenge(challenge)
                .userId(userId)
                .build();
    }
}
