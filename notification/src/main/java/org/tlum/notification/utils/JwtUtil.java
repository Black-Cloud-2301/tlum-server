package org.tlum.notification.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${authToken.jwtSecret}")
    private String JWT_SECRET;

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    public Claims decodeJwt(String token) {
        try {
            return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            LOGGER.error("Error decoding JWT", e);
            throw new RuntimeException("Error decoding JWT", e);
        }
    }
}
