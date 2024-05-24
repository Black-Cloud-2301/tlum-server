package com.kltn.authservice.payload;

import com.kltn.authservice.business.user.User;
import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private User user;
    private String refreshToken;

    public LoginResponse(String accessToken, User user, String refreshToken) {
        this.accessToken = accessToken;
        this.user = user;
        this.refreshToken = refreshToken;
    }
}
