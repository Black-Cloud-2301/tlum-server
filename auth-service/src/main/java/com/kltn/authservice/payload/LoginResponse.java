package com.kltn.authservice.payload;

import com.kltn.authservice.business.user.User;
import lombok.Data;

import java.util.Set;

@Data
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private User user;
    private String refreshToken;
    private Set<String> roles;

    public LoginResponse(String accessToken, User user, String refreshToken, Set<String> roles) {
        this.accessToken = accessToken;
        this.user = user;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }
}
