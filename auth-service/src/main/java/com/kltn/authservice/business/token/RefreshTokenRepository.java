package com.kltn.authservice.business.token;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findByTokenAndIp(String token, String ip);

    Optional<RefreshToken> findByUserId(Long userId);
}
