package com.challengers.challengeservice.cart.repository;


import com.challengers.challengeservice.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByChallengeIdAndUserId(Long challengeId, Long userId);

    List<Cart> findByChallengeId(Long challengeId);
}
