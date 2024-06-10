package com.kltn.authservice.business.user;


public interface UserService {

    User findById(Long userId);

    User findByCodeIgnoreCase(String code);

    User createUser(User user);
}
