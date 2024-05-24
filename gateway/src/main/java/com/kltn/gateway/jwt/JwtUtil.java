package com.kltn.gateway.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${authToken.jwtSecret}")
    private String JWT_SECRET;

    private final Logger log = LoggerFactory.getLogger(JwtUtil.class);


    public Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("%s => %s".formatted(e.getMessage(), e));
        }
        return null;
    }

    public void validateToken(String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
        } catch (SignatureException e) {
            log.error("Invalid JWT signature");
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }
    }
}
