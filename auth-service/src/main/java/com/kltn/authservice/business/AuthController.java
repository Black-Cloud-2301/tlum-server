package com.kltn.authservice.business;

import com.kltn.authservice.exceptions.user.UserNotFoundException;
import com.kltn.authservice.payload.LoginRequest;
import com.kltn.authservice.payload.LoginResponse;
import com.kltn.authservice.payload.RefreshTokenDto;
import com.kltn.authservice.utils.jwt.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws UserNotFoundException {
        return authService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PostMapping("/refreshToken")
    public RefreshTokenDto refreshToken(@RequestBody RefreshTokenDto refreshToken) {
        return new RefreshTokenDto(jwtUtil.refreshToken(refreshToken), refreshToken.getRefreshToken());
    }
}
