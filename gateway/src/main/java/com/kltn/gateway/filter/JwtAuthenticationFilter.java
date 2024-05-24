package com.kltn.gateway.filter;

import com.kltn.gateway.jwt.JwtTokenMalformedException;
import com.kltn.gateway.jwt.JwtTokenMissingException;
import com.kltn.gateway.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    @Value("${list-api-secured}")
    private List<String> listApiSecured;

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var apiSecured = listApiSecured.stream().anyMatch(a -> request.getURI().getPath().contains(a));
        if (!apiSecured) {
            ServerHttpResponse response = exchange.getResponse();
            if (!request.getHeaders().containsKey("Authorization")) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            var authorizations = request.getHeaders().get("Authorization");
            assert authorizations != null;
            assert !authorizations.isEmpty();
            String token = authorizations.get(0).replaceFirst("Bearer ", "");
            try {
                jwtUtil.validateToken(token);
            } catch (Exception e) {
                if (e instanceof JwtTokenMalformedException || e instanceof JwtTokenMissingException) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                }
                return response.setComplete();
            }
            var claims = jwtUtil.getClaims(token);
            request.mutate()
                    .header("userId", claims.getSubject())
                    .header("roles", String.join(", ", (claims.get("roles")).toString()))
                    .build();
        }
        return chain.filter(exchange);
    }
}