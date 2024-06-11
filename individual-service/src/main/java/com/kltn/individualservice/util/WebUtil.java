package com.kltn.individualservice.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebUtil {
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    public String getUserId() {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = header.replaceFirst("Bearer ", "");
        return jwtUtil.decodeJwt(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles() {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = header.replaceFirst("Bearer ", "");
        return (List<String>) jwtUtil.decodeJwt(token).get("roles");
    }
}
