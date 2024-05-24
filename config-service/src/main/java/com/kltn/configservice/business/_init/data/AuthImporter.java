package com.kltn.configservice.business._init.data;

import com.kltn.configservice.business._init.DataImporter;
import com.kltn.configservice.business.auth.Auth;
import com.kltn.configservice.business.auth.AuthRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthImporter extends DataImporter {
    private final AuthRepository authRepository;

    @Value("${config.current-profile}")
    private String profile;

    @Value("${config.auth.jwt-expiration-minutes}")
    private Long jwtExpirationMinutes;

    @Value("${config.auth.jwt-refresh-expiration-day}")
    private int jwtRefreshExpirationDay;

    @Value("${config.auth.key.seed}")
    private Integer seed = 1;
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public AuthImporter(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void importData() {
        if (!authRepository.existsByProfile(profile)) {
            Auth newAuth = new Auth(null, "", profile, jwtExpirationMinutes * 60 * 1000, jwtRefreshExpirationDay, seed, jwtSecret);
            authRepository.save(newAuth);
        }

    }

}
