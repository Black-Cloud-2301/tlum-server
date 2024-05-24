package com.kltn.authservice.payload;

import lombok.Getter;

@Getter
public class RefreshTokenDto {
    private String accessToken;
    private final String tokenType = "Bearer";
    private String refreshToken;

    public RefreshTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
