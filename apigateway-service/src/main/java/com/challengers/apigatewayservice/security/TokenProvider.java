package com.challengers.apigatewayservice.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    @Value("${token.accessTokenExpirationMsec}")
    private long accessTokenExpirationMsec;

    @Value("${token.accessTokenSecret}")
    private String accessTokenSecret;

    private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(accessToken);
            return true;
        } catch (SignatureException ex) {
            log.error("유효하지 않은 JWT 서명");
        } catch (MalformedJwtException ex) {
            log.error("유효하지 않은 JWT 토큰");
        } catch (ExpiredJwtException ex) {
            log.error("만료된 JWT 토큰");
        } catch (UnsupportedJwtException ex) {
            log.error("지원하지 않는 JWT 토큰");
        } catch (IllegalArgumentException ex) {
            log.error("비어있는 JWT");
        }
        return false;
    }

    private Claims getClaimsFromJwtToken(String token) {
        try {
            return Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getUserId(String token) {
        return getClaimsFromJwtToken(token).getSubject();
    }
}
