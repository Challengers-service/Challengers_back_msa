package com.challengers.userservice.security;

import com.challengers.userservice.domain.User;
import io.jsonwebtoken.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider {

    @Value("${token.accessTokenExpirationMsec}")
    private long accessTokenExpirationMsec;

    @Value("${token.accessTokenSecret}")
    private String accessTokenSecret;

    private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    public String createTokenByUserEntity(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMsec);

        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, accessTokenSecret)
                .compact();
    }
}
