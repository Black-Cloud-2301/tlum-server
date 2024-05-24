package com.kltn.authservice.utils.jwt;

import com.kltn.authservice.business.role.Role;
import com.kltn.authservice.business.token.RefreshToken;
import com.kltn.authservice.business.token.RefreshTokenRepository;
import com.kltn.authservice.business.token.TokenRefreshException;
import com.kltn.authservice.business.user.User;
import com.kltn.authservice.payload.RefreshTokenDto;
import com.kltn.authservice.utils.WebUtil;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${authToken.jwtSecret}")
    private String jwtSecret;

    @Value("${authToken.jwtExpiration}")
    private long jwtExpiration;

    @Value("${authToken.refreshExpirationDay}")
    private Integer refreshExpirationDay;

    private final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private final RefreshTokenRepository refreshTokenRepository;
    private final WebUtil webUtil;

    public JwtUtil(RefreshTokenRepository refreshTokenRepository, WebUtil webUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.webUtil = webUtil;
    }

    public String generateToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .addClaims(Map.of("roles", user.getRoles().stream().map(Role::getId).collect(Collectors.toList())))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public RefreshToken createRefreshToken(User user, String ip) {
        Optional<RefreshToken> refreshTokenDB = refreshTokenRepository.findByUserId(user.getId());
        refreshTokenDB.ifPresent(refreshTokenRepository::delete);
        RefreshToken refreshToken = new RefreshToken(null, UUID.randomUUID().toString(), Instant.now().plus(Duration.ofDays(refreshExpirationDay)), user, ip);
        return refreshTokenRepository.save(refreshToken);
    }

    public String refreshToken(RefreshTokenDto token) {
        String ip = webUtil.getClientIp();
        return refreshTokenRepository.findByTokenAndIp(token.getRefreshToken(), ip)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(this::generateToken)
                .orElseThrow(() -> new TokenRefreshException("REFRESH_TOKEN_NOT_EXISTS"));
    }

    private boolean isValidToken(String authToken) {

        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        }

        return false;
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("REFRESH_TOKEN_EXPIRED");
        }
        return token;
    }

}
