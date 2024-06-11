package com.kltn.authservice.business;

import com.kltn.authservice.business.redis.BaseRedisService;
import com.kltn.authservice.business.redis.RedisKey;
import com.kltn.authservice.business.role.RoleService;
import com.kltn.authservice.business.user.User;
import com.kltn.authservice.business.user.UserService;
import com.kltn.authservice.exceptions.user.IncorrectPasswordException;
import com.kltn.authservice.exceptions.user.UserInactiveException;
import com.kltn.authservice.exceptions.user.UserNotFoundException;
import com.kltn.authservice.payload.LoginResponse;
import com.kltn.authservice.utils.WebUtil;
import com.kltn.authservice.utils.enums.EntityStatus;
import com.kltn.authservice.utils.jwt.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final WebUtil webUtil;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final BaseRedisService baseRedisService;

    public AuthServiceImpl(UserService userService, WebUtil webUtil, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, RoleService roleService, BaseRedisService baseRedisService) {
        this.userService = userService;
        this.webUtil = webUtil;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.baseRedisService = baseRedisService;
    }

    @Override
    public LoginResponse login(String username, String password) {
        User user = userService.findByCodeIgnoreCase(username);
        String ip = webUtil.getClientIp();

        if (user.getIsActive() == EntityStatus.DELETED) throw new UserNotFoundException();
        if (user.getIsActive() == EntityStatus.INACTIVE) throw new UserInactiveException();

        var accessToken = generateToken(user, password);
        var refreshToken = jwtUtil.createRefreshToken(user, ip);
        Set<String> roles = roleService.flattenRoles(user.getRoles());

        baseRedisService.hashSet(RedisKey.USER.getKey(), user.getId().toString(), roles);

        return new LoginResponse(accessToken, user, refreshToken.getToken(), roles);
    }


    private String generateToken(User user, String password) throws IncorrectPasswordException {
        if (!passwordEncoder.matches(password, user.getPassword())) throw new IncorrectPasswordException();
        return jwtUtil.generateToken(user);
    }
}
