package com.challengers.apigatewayservice.filter;

import com.challengers.apigatewayservice.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderAnonymousFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderAnonymousFilter.Config> {

    private final TokenProvider tokenProvider;

    @Autowired
    public AuthorizationHeaderAnonymousFilter(TokenProvider tokenProvider) {
        super(Config.class);
        this.tokenProvider = tokenProvider;
    }

    static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("AuthorizationHeaderFilter Start: request -> {}", exchange.getRequest());

            HttpHeaders headers = request.getHeaders();
            if (headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                String authorizationHeader = headers.get(HttpHeaders.AUTHORIZATION).get(0);

                // JWT 토큰 판별
                String token = authorizationHeader.replace("Bearer", "");

                tokenProvider.validateAccessToken(token);

                String subject = tokenProvider.getUserId(token);

                ServerHttpRequest newRequest = request.mutate()
                        .header("userId", subject)
                        .build();

                log.info("AuthorizationHeaderFilter End");

                return chain.filter(exchange.mutate().request(newRequest).build());
            }

            log.info("AuthorizationHeaderFilter End");
            return chain.filter(exchange);
        };
    }

    // Mono(단일 값), Flux(다중 값) -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String errorMsg, HttpStatus httpStatus) {
        log.error(errorMsg);

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }
}
