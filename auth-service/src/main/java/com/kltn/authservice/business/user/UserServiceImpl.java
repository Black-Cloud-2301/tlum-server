package com.kltn.authservice.business.user;

import com.kltn.authservice.exceptions.user.UserAlreadyExistsException;
import com.kltn.authservice.exceptions.user.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User findByCodeIgnoreCase(String code) {
        return userRepository.findByCodeIgnoreCase(code).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByCodeIgnoreCase(user.getCode())) throw new UserAlreadyExistsException();
        user.setCode(user.getCode().toUpperCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
