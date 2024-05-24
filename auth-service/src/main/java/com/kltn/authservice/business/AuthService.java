package com.kltn.authservice.business;

import com.kltn.authservice.payload.LoginResponse;

public interface AuthService {
    LoginResponse login(String username, String password);

}
